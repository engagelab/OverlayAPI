package controllers;

import static play.libs.Json.toJson;
import helpers.FeatureCollection;

import java.util.ArrayList;
import java.util.List;
import models.Feature;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;



/**
 * @author Muhammad Fahied
 */
public class Users extends Controller{

	
	
	public static Result fetchGeoFeaturesByUser(String facebook_id)
	{
		User user = User.byfacebook_id(facebook_id);
		if (user == null) {
			List<String> empty = new ArrayList<String>();
			return ok(toJson(empty));
		}
		
		List<Feature> featureslList = user.features;
		FeatureCollection features = new FeatureCollection(featureslList);
		return ok(toJson(features));
	}
	
	public static void saveFeatureRefForUser(String facebook_id,String full_name, Feature feature)
	{
		
			if (User.byfacebook_id(facebook_id)==null) {
				User user = new User(facebook_id, full_name);
				user.features.add(feature);
				user.update();
			}
			else {
				User user = User.byfacebook_id(facebook_id);
				user.features.add(feature);
				user.update();

		}
	}
	
	
	
	
}
