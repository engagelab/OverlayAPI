package controllers;

import play.mvc.*;

/**
 * @author Muhammad Fahied
 */

public class Application extends Controller {
  
    public static Result index() {
    	
    	//instagram://media?id=341095710785366464_7549441
    	return ok("Overlay API Running");
    }
  
}
