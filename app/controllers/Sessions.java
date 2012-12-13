package controllers;

import static play.libs.Json.toJson;
import geometry.Geometry;
import geometry.Point;
import helpers.TwitterHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import models.Feature;
import models.Session;
import play.mvc.Controller;
import play.mvc.Result;



public class Sessions extends Controller{	
	
	public static Result create() throws JsonParseException, JsonMappingException, IOException
	{
	
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = ctx().request().body().asJson();
		
		JsonNode propertiesNode = node.findPath("properties");
		TypeReference<HashMap<String, Object>> collectionTypeObject = new TypeReference<HashMap<String, Object>>(){};
		HashMap<String, Object> properties = mapper.readValue(propertiesNode, collectionTypeObject);
		
		String contexual_info = properties.get("contextual_info").toString();
		
		//fetch tags from the contextual information and save them in properties object
		Set<String> tags = TwitterHelper.searchHashTags(contexual_info);
		properties.put("tags", tags);
	
		// Retrieve the bounding box coordinates and bind to Form Request
		JsonNode boxNode = propertiesNode.findPath("boundingBox");
		TypeReference<double[]> collectionTypeDouble = new TypeReference<double[]>(){};
		double[]  boundingBox =  mapper.readValue(boxNode, collectionTypeDouble);
		int nPOIs = (Integer) properties.get("nPOIs");
		
		List<Feature> features = new ArrayList<Feature>();
		if (nPOIs > 0) 
		{
			features = createRandomPOIsWithinBoundingBox(boundingBox, nPOIs);
		}
			
		Session session = new Session();
		session.features = features;
		session.properties = properties;
		session.insert();
		return ok(toJson(session));

	}
	
	
	
	// TODO: delete features belong to this project
	public static Result delete(String id, String user_id) {
		
		Session session = Session.find().byId(id);
		if(session == null)
		{
			return status(404, "NOT_FOUND");
		}
		
		JsonNode user = toJson( session.properties.get("user"));
		String owner_id = user.get("id").asText();
		if (user_id == owner_id) {
			session.delete();
			return status(200, "OK");
		}
		
		return status(403, "FORBIDDEN");
		
	}
	
	
	public static Result listAllProjects() 
	{
		List<Session> sessions = Session.find().all();
		
		return ok(toJson(sessions));
		
	}
	
	
	
	/*
	 * Pagination: db.companies.find().skip(NUMBER_OF_ITEMS * (PAGE_NUMBER - 1)).limit(NUMBER_OF_ITEMS )
	 * */
	public static Result fetchProjectById(String id) 
	{
		Session session = Session.find().byId(id);
		List<Feature> instaFeatures;
		List<Feature> overlayFeatures = new ArrayList<Feature>();
//		if (project != null) 
//		{
//			double lng1 = project.boundingBox[0];
//			double lat1 = project.boundingBox[1];
//			double lng2 = project.boundingBox[2];
//			double lat2 = project.boundingBox[3];
//			overlayFeatures= project.features;
//			try {
//				instaFeatures = InstagramParser.searchInstaPOIsByBBox(lng1, lat1, lng2, lat2);
//				overlayFeatures.addAll(instaFeatures);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		return ok(toJson(overlayFeatures));
		
	}
	
	
	
	
	
	
	
	
	public static Result contentViewBySessionId(String id) {
		
		Session session = Session.find().byId(id);
		List<Feature> instaFeatures;
//		List<Feature> overlayFeatures = new ArrayList<Feature>();
//		if (project != null) 
//		{
//			double lng1 = project.boundingBox[0];
//			double lat1 = project.boundingBox[1];
//			double lng2 = project.boundingBox[2];
//			double lat2 = project.boundingBox[3];
//			overlayFeatures= project.features;
//			try {
//				instaFeatures = InstagramParser.searchInstaPOIsByBBox(lng1, lat1, lng2, lat2);
//				overlayFeatures.addAll(instaFeatures);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		return ok(toJson(session.features));
		
	}
	
	
	
	
	
	
	
	
	
		
	
	
	/**
     * Handle the 'new Session form' submission 
     */
//    public static Result save() {
//        Form<Session> projectForm = form(Session.class).bindFromRequest();
//        if(projectForm.hasErrors()) {
//            return badRequest(createForm.render(projectForm));
//        }
//        projectForm.get().insert();
//        //flash("success", "Session " + projectForm.get().title + " has been created");
//        return ok(createForm.render(projectForm));
//    }
    
    
    
    
    
    
    
    
    /* generate random number between the range
	 * Min + (int)(Math.random() * ((Max - Min) + 1))
	 * 
	 * double random = new Random().nextDouble();
		double result = start + (random * (end - start));
	 * */
	public static  List<Feature> createRandomPOIsWithinBoundingBox(double[] boundingBox, int nPOIs) 
	{
		
		double lng1 = boundingBox[0];
		double lat1 = boundingBox[1];
		double lng2 = boundingBox[2];
		double lat2 = boundingBox[3];
		
		List<Feature> features = new ArrayList<Feature>();
		
		    for (int idx = 1; idx <= nPOIs; ++idx)
		    {
		    	double random = new Random().nextDouble();
		    	double rlat = lat1 + (random * ((lat2 - lat1)));
		    	double rlng = lng1 + (random * ((lng2 - lng1)));
		    	Geometry geometry = new Point(rlng, rlat);
		    	Feature feature = new Feature(geometry);
		    	feature.properties.put("source_type", "empty");
		    	feature.properties.put("name", "empty");
		    	features.add(feature);
		    	feature.insert();
		    }
        return features;
    }
	
	
	
    
}
