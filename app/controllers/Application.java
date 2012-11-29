package controllers;

import play.mvc.*;

//import views.html.*;

/**
 * @author Muhammad Fahied
 */

public class Application extends Controller {
  
    public static Result index() {
        return ok("app ready!");
    }
  
}
