package controllers;

import leodagdag.play2morphia.Blob;
import leodagdag.play2morphia.MorphiaPlugin;
import models.Feature;
import models.Session;
import net.coobird.thumbnailator.Thumbnails;

import data.BasicImage;
import data.Images;
import external.Constants;
import external.InstagramParser;
import geometry.Geometry;
import geometry.Point;
import helpers.FeatureCollection;
import helpers.TwitterHelper;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.sun.org.apache.bcel.internal.generic.NEW;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Results;
import static play.libs.Json.toJson;

/**
 * @author Muhammad Fahied
 */

public class Features extends Controller {

	public static Result createGeoFeature() throws JsonParseException, JsonMappingException, IOException {

		//String low_resolution = convertToInstagramImage(filePart.getFile(),filePart.getContentType());
		//String low_resolution = saveImageFile(convertToInstagramImage(filePart.getFile(), 
		//									filePart.getContentType()),filePart.getContentType());
 
		
		FilePart jsonFilePart = ctx().request().body().asMultipartFormData().getFile("feature");
		// Convert json file to JsonNode
		ObjectMapper mapperj = new ObjectMapper();
		BufferedReader fileReader = new BufferedReader(
			new FileReader(jsonFilePart.getFile()));
		
		JsonNode node = mapperj.readTree(fileReader);
		ObjectMapper mapper = new ObjectMapper();
		
		
		JsonNode coordinatesNode = node.findPath("coordinates");
		TypeReference<Double[]> collectionTypeD = new TypeReference<Double[]>(){};
		Double[]  coordinates =  mapper.readValue(coordinatesNode, collectionTypeD);
		Geometry geometry = new Point(coordinates[0], coordinates[1]);
		
		Feature geoFeature = new Feature(geometry);
		
		JsonNode propertiesNode = node.findPath("properties");
		TypeReference<HashMap<String, Object>> collectionType = new TypeReference<HashMap<String, Object>>(){};
		HashMap<String, Object> properties = mapper.readValue(propertiesNode, collectionType);
			
		String description = (String) properties.get("description");
		
		//Formulate the label of the POI, using first sentence in the description
		String delims = "[.,?!]+";
		String[] tokens = description.split(delims);
		String name = tokens[0];
		properties.put("name", name);

		
		Set<String> tags = TwitterHelper.searchHashTags(description);
		properties.put("tags", tags);
		
		
		//HashMap<String, Object> images = new HashMap<String, Object>(3);
		
		//images.put("standard_resolution", new BasicImage(Constants.SERVER_NAME+"/image/"+standard_resolution));
		
		//properties.put("images", images);
		
		//properties.put("images", im);
		
		String standard_resolution = "";
		// Extract BasicImage from Multipart data
		if (ctx().request().body().asMultipartFormData().getFile("picture") != null) {
			FilePart filePart = ctx().request().body().asMultipartFormData().getFile("picture");
			standard_resolution = saveImageFile(filePart.getFile(), filePart.getContentType());
			properties.put("standard_resolution", Constants.SERVER_NAME+"/image/"+standard_resolution);

		}
		
		properties.put("source_type", "overlay");
		
		//HTML Content url for the Feature
		properties.put("descr_url", Constants.SERVER_NAME+"/content/"+geoFeature.id);
		
		
		//add timestamp
		Date date = new Date();
	    long dateInLong = date.getTime();
	    properties.put("created_time", dateInLong);
		
		geoFeature.setProperties(properties);
		
		geoFeature.insert();
		
		//Add this feature to perticular session
		if (propertiesNode.get("session_id") != null) {
			String seesion_id = propertiesNode.get("session_id").asText();
			Session session = Session.find().byId(seesion_id);
			if (session != null) {
				session.features.add(geoFeature);
			}
			
		}
		

		//TODO: move these task to Feature Model
		//Save feature reference to individual tags
		HashTagManager.saveFeatureRefInHashTable(tags, geoFeature);
		
		//Save Feature reference for perticular user
		JsonNode user = node.findPath("user");
		if (!(user.isNull())) {
			Users.saveFeatureRefForUser(user.get("id").toString(), user.get("full_name").toString(),geoFeature);
		}
		
		return ok(toJson(geoFeature));
	}
	
public static Result updateGeoFeature() throws JsonParseException, JsonMappingException, IOException {
		
	// Extract BasicImage from Multipart data
	FilePart filePart = ctx().request().body().asMultipartFormData().getFile("picture");
	String standard_resolution = saveImageFile(filePart.getFile(), filePart.getContentType());
	//String low_resolution = convertToInstagramImage(filePart.getFile(),filePart.getContentType());
	//String low_resolution = saveImageFile(convertToInstagramImage(filePart.getFile(), 
	//									filePart.getContentType()),filePart.getContentType());
	
	
	
	
	FilePart jsonFilePart = ctx().request().body().asMultipartFormData().getFile("feature");
	// Convert json file to JsonNode
	ObjectMapper mapperj = new ObjectMapper();
	BufferedReader fileReader = new BufferedReader(
		new FileReader(jsonFilePart.getFile()));
	
	JsonNode node = mapperj.readTree(fileReader);
	
	//JsonNode node = ctx().request().body().asJson();
	ObjectMapper mapper = new ObjectMapper();
	
	//TODO: parse geometry object directly to Java Class from jsonNode
	//JsonNode geometryNode = node.findPath("geometry");
	//Point point = fromJson(geometry,Point.class);
	//String typeString = geometryNode.get("type").asText();
	//Feature newFeature = fromJson(node, Feature.class);
	
//	JsonNode coordinatesNode = node.findPath("coordinates");
//	TypeReference<Double[]> collectionTypeD = new TypeReference<Double[]>(){};
//	Double[]  coordinates =  mapper.readValue(coordinatesNode, collectionTypeD);
//	Geometry geometry = new Point(coordinates[0], coordinates[1]);
	
	String id = node.get("id").asText();
	Feature geoFeature = Feature.find().byId(id);
	
	
	
	JsonNode propertiesNode = node.findPath("properties");
	TypeReference<HashMap<String, Object>> collectionType = new TypeReference<HashMap<String, Object>>(){};
	HashMap<String, Object> properties = mapper.readValue(propertiesNode, collectionType);
		
	String description = (String) properties.get("description");
	
	//Formulate the label of the POI, using first sentence in the description
	String delims = "[.,?!]+";
	String[] tokens = description.split(delims);
	String name = tokens[0];
	properties.put("name", name);

	// TODO: Transfer this process to view content: Parse the decription tweet to plain HTML
	//String HTMLdescriptionString = TwitterHelper.parse(description);
	//properties.put("description", HTMLdescriptionString);
	
	Set<String> tags = TwitterHelper.searchHashTags(description);
	properties.put("tags", tags);
	
	//save url to both standard and instagram image
	

	//Images images = new Images(standard_resolution);
//	properties.put("images", toJson(images));
	
	//properties.put("low_resolution", "http://localhost:9000/image/"+low_resolution);
	
	properties.put("source_type", "overlay");
	
	//HTML Content url for the Feature
	properties.put("descr_url", Constants.SERVER_NAME+"/content/"+geoFeature.id);
	
	//add timestamp
	Date date = new Date();
    long dateInLong = date.getTime();
    properties.put("created_time", dateInLong);
	
	geoFeature.setProperties(properties);
	
	geoFeature.update();
	
	//Add this feature to perticular session
	String seesion_id = propertiesNode.get("session_id").asText();
	Session session = Session.find().byId(seesion_id);
	if (session != null) {
		session.features.add(geoFeature);
	}

	//TODO: move these task to Feature Model
	//Save feature reference to individual tags
	HashTagManager.saveFeatureRefInHashTable(tags, geoFeature);
	
	//Save Feature reference for perticular user
	JsonNode user = node.findPath("user");
	if (!(user.isNull())) {
		Users.saveFeatureRefForUser(user.get("id").toString(), user.get("full_name").toString(),geoFeature);
	}
	
	return ok(toJson(geoFeature));
	}
	




	
	
	
	public static Result fetchAllGeoFeautres()
	{
		List<Feature> featureslList = Feature.find().all();
		FeatureCollection features = new FeatureCollection(featureslList);
		return ok(toJson(features));
	}
	
	
	
	
	
	
	
	
	/* To enble geoo spacial indexing
	 * db.Feature.ensureIndex({"geometry.coordinates":"2d"});
	 * */
	public static Result geoFeaturesInBoundingBox(String lng1, String lat1, String lng2, String lat2) throws Exception
	{
		//Double [][] boundingbox = new Double[][]{{Double.valueOf(lng1),Double.valueOf(lat1)},{Double.valueOf(lng2),Double.valueOf(lat2)}};
		Double lng11 = Double.valueOf(lng1);
		Double lat11 = Double.valueOf(lat1);
		Double lng22 = Double.valueOf(lng2);
		Double lat22 = Double.valueOf(lat2);
	
		List<Feature> features = Feature.find().disableValidation().field("geometry.coordinates").within(lng11, lat11, lng22, lat22).asList();	
		List<Feature> instaPOIs = InstagramParser.searchInstaPOIsByBBox(lng11, lat11, lng22, lat22);
		features.addAll(instaPOIs);
		
		FeatureCollection collection = new FeatureCollection(features);
		
		return ok(toJson(collection));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static Result deleteAGeaoFeature(String id, String author_id) 
	{
		Feature feature = Feature.find().byId(id);
		if(feature == null)
		{
			return status(404, "NOT_FOUND");
		}
		
		String real_author_id = (String) feature.properties.get("author_id");
		if (author_id == real_author_id) {
			feature.delete();
			return status(200, "OK");
		}
		
		return status(403, "FORBIDDEN");
	}
	
	
	
	
	
	public static String saveImage(FilePart filePart)
	{
		
		if (filePart.getFile() == null)
			return "";

		Blob imageBlob = new Blob(filePart.getFile(), filePart.getContentType());
		GridFSFile file = imageBlob.getGridFSFile();
		file.save();
		return  file.getId().toString();
	}
	
	
	
	
	
	
	public static String saveImageFile(File file, String content_type)
	{
		
		if (file == null)
			return "";

		Blob imageBlob = new Blob(file, content_type);
		GridFSFile image = imageBlob.getGridFSFile();
		image.save();
		return  image.getId().toString();
	}
	

	
	
	
	
	
	public static Boolean deleteImage(String id)
	{
		MorphiaPlugin.gridFs().remove(new BasicDBObject("id", new ObjectId(id)));
		return true;
	}
	
	
	
	
	
	
	
	
	public static Result showImage(String id) throws IOException {
		
		GridFSDBFile file = MorphiaPlugin.gridFs().findOne(new ObjectId(id));
		
		byte[] bytes = IOUtils.toByteArray(file.getInputStream());
		
		return Results.ok(bytes).as(file.getContentType());
		
	}
	
	
	//Instagram take only images with resolution 612 x 612
	public static String  convertToInstagramImage(File file, String content_type) throws IOException 
	{
		//GridFSDBFile gfile = MorphiaPlugin.gridFs().findOne(new ObjectId(standard_resolution));
		BufferedImage src = ImageIO.read(file);
		int height = src.getHeight();
		int width = src.getWidth();
		BufferedImage dest = null;
		if (height > width) {
			 dest = src.getSubimage(0, 0, width,width);
		}
		else {
			dest = src.getSubimage(0, 0, height,height);
		}
		
		BufferedImage os = null;
	    
	    try {
	    	os = Thumbnails.of(dest).size(612, 612).asBufferedImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	   // File file = instagramBufferedImage.;
	    ImageIO.write(os, content_type, file);
	    
	    Blob imageBlob = new Blob(file, content_type);
		GridFSFile image = imageBlob.getGridFSFile();
		image.save();
		return  image.getId().toString();

	   }

	
	/*  HTTP STatus Codes
	  public static final int OK = 200;
	  public static final int CREATED = 201;
	  public static final int ACCEPTED = 202;
	  public static final int PARTIAL_INFO = 203;
	  public static final int NO_RESPONSE = 204;
	  public static final int MOVED = 301;
	  public static final int FOUND = 302;
	  public static final int METHOD = 303;
	  public static final int NOT_MODIFIED = 304;
	  public static final int BAD_REQUEST = 400;
	  public static final int UNAUTHORIZED = 401;
	  public static final int PAYMENT_REQUIERED = 402;
	  public static final int FORBIDDEN = 403;
	  public static final int NOT_FOUND = 404;
	  public static final int INTERNAL_ERROR = 500;
	  public static final int NOT_IMPLEMENTED = 501;
	  public static final int OVERLOADED = 502;
	  public static final int GATEWAY_TIMEOUT = 503;
	   * 
	   * 
	   * */
}

