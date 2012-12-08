package controllers;

import play.mvc.*;
import views.html.*;

/**
 * @author Muhammad Fahied
 */

public class Application extends Controller {
  
    public static Result index() {
    	
    	//instagram://media?id=341095710785366464_7549441
    	return ok(maps.render());
    }
  
}
