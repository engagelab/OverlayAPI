package controllers;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import static play.libs.Json.fromJson;

import models.Feature;
import geometry.Geometry;
import geometry.Point;
import play.mvc.Controller;
import play.mvc.Result;
import static play.libs.Json.toJson;

public class Features extends Controller {

	public static Result createPOI() throws JsonParseException, JsonMappingException, IOException {
		JsonNode node = ctx().request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		
		//JsonNode geometryNode = node.findPath("geometry");
		
//		String typeString = geometryNode.get("type").asText();
		
		JsonNode coordinates = node.findPath("coordinates");
		TypeReference<Double[]> collectionTypeD = 
			    new TypeReference<Double[]>(){};
			Double[]  coDouble = 
			    mapper.readValue(coordinates, collectionTypeD);
		Geometry geometry = new Point(coDouble[0], coDouble[1]);
		
		JsonNode properties = node.findPath("properties");
		TypeReference<HashMap<String, Object>> collectionType = 
			    new TypeReference<HashMap<String, Object>>(){};
			HashMap<String, String> proMap = 
			    mapper.readValue(properties, collectionType);

		//Point point = fromJson(geometry,Point.class);
		
			
		Feature geoFeature = new Feature(geometry);
		geoFeature.setProperties(proMap);
		geoFeature.insert();
		return ok(toJson(geoFeature));
	}

}

/*
 {
     "type": "Feature",
     "geometry":
                 {
                    "type": "Point",
                    "coordinates": [102.0, 0.5]
                 },
     "properties":
                 {
           “name” : “Server will generate the name of POI by parsing the TEXT using some logic”
                    "description": "text including label  and hashtags that can be the first sentence or some other logical segment",
            "author": "author name from facebook",
             "authorId": "author unique Id from facebook",
                    “tags”: “a list of string separated by space will be made by server by parsing the text”
                     “descr_url”: “created by server which will lead to HTML page showing the contents of PIO”
             “icon_url” : “icon of POI representing the source [empty, overlay, instagram, twtiter, etc]”
             “source_type”: “source [empty, overlay, instagram, twtiter, etc]”
                 }
}
  
 */