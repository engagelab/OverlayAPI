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

	
	
	public static Result fetchGeoFeaturesByUser(String userID)
	{
		User user = User.find().byId(userID);
		if (user == null) {
			List<String> empty = new ArrayList<String>();
			return ok(toJson(empty));
		}
		
		List<Feature> featureslList = user.features;
		FeatureCollection features = new FeatureCollection(featureslList);
		return ok(toJson(features));
	}
	
	public static void saveFeatureRefForUser(String id,String full_name, Feature feature)
	{
		
			if (User.find().byId(id)==null) {
				User user = new User(id, full_name);
				user.features.add(feature);
				user.update();
			}
			else {
				User user = User.find().byId(id);
				user.features.add(feature);
				user.update();

		}
	}
	
	
	
	
}
