package controllers;

import static play.libs.Json.toJson;
import helpers.FeatureCollection;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import models.Feature;
import models.HashTagTable;
import models.MappingSession;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;;



public class MapingSessions extends Controller{
	
	public static Result create() throws JsonParseException, JsonMappingException, IOException
	{
		double[] box = {100, 200, 300,400};
	
		MappingSession session = new MappingSession("Testing Session", "this is test",box , 12, "Toyen");
		
		JsonNode data = toJson(session);
	
		Form<MappingSession> sessionForm = form(MappingSession.class);
	
		//sessionForm.data().put("boundingBox", boundingBox.toString());
		Form<MappingSession> sessionForm2 = sessionForm.bind(data, "boundingbox","title");
		return ok(createForm.render(sessionForm2));

	}
	
	
	public static Result create2() throws JsonParseException, JsonMappingException, IOException
	{
	
		ObjectMapper mapper = new ObjectMapper();
		JsonNode data = ctx().request().body().asJson();
	
		// Retrieve the bounding box coordinates and bind to Form Request
		JsonNode boxNode = data.findPath("boundingBox");
		TypeReference<Double[]> collectionType = new TypeReference<Double[]>(){};
		Double[]  boundingBox =  mapper.readValue(boxNode, collectionType);
		
	
		Form<MappingSession> sessionForm = form(MappingSession.class);
	
		//sessionForm.data().put("boundingBox", boundingBox.toString());
		Form<MappingSession> sessionForm2 = sessionForm.bind(data, "boundingbox","title");
		return ok(createForm.render(sessionForm2));

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
