package controllers;

import models.Feature;
import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Contents extends Controller{
	
	
	public static Result contentOfFeature(String id)
	{
		Feature feature = Feature.find().byId(id);
		
		Html description = new Html(feature.properties.get("description").toString());
		return ok(index.render(feature,description));
	}
	


}
