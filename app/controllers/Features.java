package controllers;

import models.Feature;
import geometry.Geometry;
import geometry.Point;
import helpers.TwitterHelper;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.typesafe.config.ConfigException.Parse;

import static play.libs.Json.fromJson;
import play.mvc.Controller;
import play.mvc.Result;
import static play.libs.Json.toJson;



public class Features extends Controller {

	public static Result createPOI() throws JsonParseException, JsonMappingException, IOException {
		JsonNode node = ctx().request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		
		//TODO: parse geometry object directly to Java Class from jsonNode
		//JsonNode geometryNode = node.findPath("geometry");
		//Point point = fromJson(geometry,Point.class);
		//String typeString = geometryNode.get("type").asText();
		
		
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
		
		List<String> tags = TwitterHelper.searchHashTags(description);
		proMap.put("tags", tags);
			
		Feature geoFeature = new Feature(geometry);
		geoFeature.setProperties(proMap);
		geoFeature.insert();
		return ok(toJson(geoFeature));
	}

}

