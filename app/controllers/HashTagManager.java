package controllers;

import static play.libs.Json.toJson;
import helpers.FeatureCollection;

import java.util.List;
import java.util.Set;


import models.Feature;
import models.HashTagTable;
import play.mvc.Controller;
import play.mvc.Result;

public class HashTagManager extends Controller{

	
	
	public static Result fetchGeoFeaturesByTag(String hashTag)
	{
		HashTagTable table = HashTagTable.byTag(hashTag);
		if (table == null) {
			//JSONObject empty = new JSONObject();
			return ok(toJson(""));
		}
		
		List<Feature> featureslList = table.features;
		FeatureCollection features = new FeatureCollection(featureslList);
		return ok(toJson(features));
	}
	
	public static void saveFeatureRefInHashTable(Set<String> tags, Feature feature)
	{
		for (String hashTag : tags) {
			if (HashTagTable.byTag(hashTag)==null) {
				HashTagTable table = new HashTagTable(hashTag);
				table.features.add(feature);
				table.update();
			}
			else {
				HashTagTable table = HashTagTable.byTag(hashTag);
				table.features.add(feature);
				table.update();
				
			}
			
		}
	}
	
	
	
	
}
