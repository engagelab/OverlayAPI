package controllers;

import static play.libs.Json.toJson;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import external.InstagramParser;

import models.Feature;
import models.HashTagTable;
import play.mvc.Controller;
import play.mvc.Result;

public class HashTagManager extends Controller{

	
	
	public static Result fetchGeoFeaturesByTag(String hashTag)
	{
		HashTagTable hashTagTable = HashTagTable.byTag(hashTag);
		List<Feature> features = new ArrayList<Feature>();
		
		if (hashTagTable != null) {
			List<Feature> overlayFeatures = hashTagTable.features;
			features.addAll(overlayFeatures);
		}
		
		List<Feature> instaFeatures;
		try {
			instaFeatures = InstagramParser.searchInstaPOIsByTag(hashTag);
			features.addAll(instaFeatures);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		FeatureCollection featureCollection = new FeatureCollection(features);
		return ok(toJson(featureCollection));

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
	
	
	public static void removeFeatureRefInHashTable(Set<String> tags, Feature feature)
	{
		for (String hashTag : tags) {
			if (HashTagTable.byTag(hashTag)!=null) {
				
				HashTagTable table = HashTagTable.byTag(hashTag);
				table.removeFeature(feature);
				table.update();
			}
			
		}
	}
	
	
	
	
	
}
