package controllers;

import static play.libs.Json.toJson;
import helpers.FeatureCollection;

import java.util.List;
import models.Feature;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;



/**
 * @author Muhammad Fahied
 */
public class Users extends Controller{

	
	
	public static Result fetchGeoFeaturesByUser(String username)
	{
		User user = User.byUsername(username);
		if (user == null) {
			//JSONObject empty = new JSONObject();
			return ok(toJson(""));
		}
		
		List<Feature> featureslList = user.features;
		FeatureCollection features = new FeatureCollection(featureslList);
		return ok(toJson(features));
	}
	
	public static void saveFeatureRefForUser(String username,String full_name, Feature feature)
	{
		
			if (User.byUsername(username)==null) {
				User user = new User(username, full_name);
				user.features.add(feature);
				user.update();
			}
			else {
				User user = User.byUsername(username);
				user.features.add(feature);
				user.update();

		}
	}
	
	
	
	
}
