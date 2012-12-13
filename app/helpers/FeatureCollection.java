package helpers;

import java.util.List;

import models.Feature;


/**
 * @author Muhammad Fahied
 */



public class FeatureCollection {
	public  String type = "FeatureCollection";
	private List<Feature> features;
	
	public FeatureCollection(List<Feature> features) {
		this.features = features;
	}
	
	public Feature[] getFeatures() {
		return features.toArray(new Feature[0]);
	}
}