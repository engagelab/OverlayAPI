package controllers;

import static play.libs.Json.toJson;
import helpers.TwitterHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import external.InstagramParser;

import models.Feature;
import models.MappingSession;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;



public class MapingSessions extends Controller{
	

	
	
//	public static Result createX() throws JsonParseException, JsonMappingException, IOException
//	{
//	
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode data = ctx().request().body().asJson();
//	
//		// Retrieve the bounding box coordinates and bind to Form Request
//		JsonNode boxNode = data.findPath("boundingBox");
//		TypeReference<Double[]> collectionType = new TypeReference<Double[]>(){};
//		Double[]  boundingBox =  mapper.readValue(boxNode, collectionType);
//		
//	
//		Form<MappingSession> sessionForm = form(MappingSession.class);
//	
//		//sessionForm.data().put("boundingBox", boundingBox.toString());
//		Form<MappingSession> sessionForm2 = sessionForm.bind(data, "boundingbox","title");
//		return ok(createForm.render(sessionForm2));
//
//	}
	
	
	public static Result create() throws JsonParseException, JsonMappingException, IOException
	{
	
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = ctx().request().body().asJson();
		
		JsonNode properties = node.findPath("properties");
		TypeReference<HashMap<String, Object>> collectionType = new TypeReference<HashMap<String, Object>>(){};
		HashMap<String, Object> proMap = mapper.readValue(properties, collectionType);
		
	
		// Retrieve the bounding box coordinates and bind to Form Request
		JsonNode boxNode = data.findPath("boundingBox");
		TypeReference<double[]> collectionType = new TypeReference<double[]>(){};
		double[]  boundingBox =  mapper.readValue(boxNode, collectionType);
		
		String title = data.get("title").asText();
		String contexual_info = data.get("contextual_info").asText();
		
		Set<String> tags = TwitterHelper.searchHashTags(contexual_info);
		
		int nPOIs = data.get("nPOIs").asInt();
		String facebook_group_name = data.get("facebook_group_name").asText();
		String facebook_id = data.get("facebook_id").asText();
		
		MappingSession session = new MappingSession();
		session.insert();
		return ok(toJson(session));

	}
	
	
	
	// TODO: delete features belong to this session
	public static Result delete(String id, String author_id) {
		
		MappingSession session = MappingSession.find().byId(id);
		if(session == null)
		{
			return status(404, "NOT_FOUND");
		}
		
		String real_author_id = (String) session.facebook_id;
		if (author_id == real_author_id) {
			session.delete();
			return status(200, "OK");
		}
		
		return status(403, "FORBIDDEN");
		
	}
	
	
	public static Result listAllSessions() 
	{
		List<MappingSession> sessions = MappingSession.find().all();
		
		return ok(toJson(sessions));
		
	}
	
	
	
	/*
	 * Pagination: db.companies.find().skip(NUMBER_OF_ITEMS * (PAGE_NUMBER - 1)).limit(NUMBER_OF_ITEMS )
	 * */
	public static Result fetchSessionById(String id) 
	{
		MappingSession session = MappingSession.find().byId(id);
		List<Feature> instaFeatures;
		List<Feature> overlayFeatures = new ArrayList<Feature>();
		if (session != null) 
		{
			double lng1 = session.boundingBox[0];
			double lat1 = session.boundingBox[1];
			double lng2 = session.boundingBox[2];
			double lat2 = session.boundingBox[3];
			overlayFeatures= session.features;
			try {
				instaFeatures = InstagramParser.searchInstaPOIsByBBox(lng1, lat1, lng2, lat2);
				overlayFeatures.addAll(instaFeatures);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ok(toJson(overlayFeatures));
		
	}
	
	
	
	
	
	
	
	
	public static Result contentViewBySessionId(String id) {
		
		MappingSession session = MappingSession.find().byId(id);
		List<Feature> instaFeatures;
		List<Feature> overlayFeatures = new ArrayList<Feature>();
		if (session != null) 
		{
			double lng1 = session.boundingBox[0];
			double lat1 = session.boundingBox[1];
			double lng2 = session.boundingBox[2];
			double lat2 = session.boundingBox[3];
			overlayFeatures= session.features;
			try {
				instaFeatures = InstagramParser.searchInstaPOIsByBBox(lng1, lat1, lng2, lat2);
				overlayFeatures.addAll(instaFeatures);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ok(toJson(session.features));
		
	}
	
	
	
	
	
	
	
	
	
		
	
	
	/**
     * Handle the 'new Session form' submission 
     */
    public static Result save() {
        Form<MappingSession> sessionForm = form(MappingSession.class).bindFromRequest();
        if(sessionForm.hasErrors()) {
            return badRequest(createForm.render(sessionForm));
        }
        sessionForm.get().insert();
        flash("success", "Session " + sessionForm.get().title + " has been created");
        return ok(createForm.render(sessionForm));
    }
    
}
