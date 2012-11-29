package controllers;

import models.Feature;

import geometry.Geometry;
import geometry.Point;
import helpers.FeatureCollection;
import helpers.TwitterHelper;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;



import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import static play.libs.Json.toJson;

/**
 * @author Muhammad Fahied
 */

public class Features extends Controller {

	public static Result createGeoFeature() throws JsonParseException, JsonMappingException, IOException {
		
		// Extract Image from Multipart data
		FilePart filePart = ctx().request().body().asMultipartFormData().getFile("picture");
		
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
		
		JsonNode coordinatesNode = node.findPath("coordinates");
		TypeReference<Double[]> collectionTypeD = new TypeReference<Double[]>(){};
		Double[]  coordinates =  mapper.readValue(coordinatesNode, collectionTypeD);
		Geometry geometry = new Point(coordinates[0], coordinates[1]);
		
		JsonNode properties = node.findPath("properties");
		TypeReference<HashMap<String, Object>> collectionType = new TypeReference<HashMap<String, Object>>(){};
		HashMap<String, Object> proMap = mapper.readValue(properties, collectionType);
			
		String description = (String) proMap.get("description");
		
		//Formulate the label of the POI, using first sentence in the description
		String delims = "[.,?!]+";
		String[] tokens = description.split(delims);
		String name = tokens[0];
		proMap.put("name", name);

		// Parse the decription tweet to plain HTML
		String HTMLdescriptionString = TwitterHelper.parse(description);
		proMap.put("description", HTMLdescriptionString);
		
		Set<String> tags = TwitterHelper.searchHashTags(description);
		proMap.put("tags", tags);
			
		Feature geoFeature = new Feature(geometry);
		geoFeature.setProperties(proMap);
		geoFeature.insert();
		return ok(toJson(geoFeature));
	}
	
	
	
	
	
	
	public static Result fetchAllGeoFeautres()
	{
		List<Feature> featureslList = Feature.find().all();
		FeatureCollection features = new FeatureCollection(featureslList);
		return ok(toJson(features));
	}
	
	
	public static Result geoFeaturesInBoundingBox(String lon1, String lat1, String lon2, String lat2)
	{
		//Double [][] boundingbox = new Double[][]{{Double.valueOf(lon1),Double.valueOf(lat1)},{Double.valueOf(lon2),Double.valueOf(lat2)}};
		Double lon11 = Double.valueOf(lon1);
		Double lat11 = Double.valueOf(lat1);
		Double lon22 = Double.valueOf(lon2);
		Double lat22 = Double.valueOf(lat2);
	
		List<Feature> features = Feature.find().disableValidation().field("geometry.coordinates").within(lon11, lat11, lon22, lat22).asList();	
	
		return ok(toJson(features));
	}
	
	

}

