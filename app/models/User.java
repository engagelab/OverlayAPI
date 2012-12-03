package models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;

import leodagdag.play2morphia.Model;



/**
 * @author Muhammad Fahied
 */

@Entity
public class User extends Model
{

	
	@Id
	public String id = new ObjectId().toString();
	
	public String username;
	public String full_name;
	
	@Reference
	public List<Feature> features;
	
	
	
	public static Model.Finder<String, User> find()
	{
		
		return new Model.Finder<String, User>(String.class, User.class);
	
	}
	
	
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	

	
	public User(String username, String full_name) {
		
		if (features == null) {
			features = new ArrayList<Feature>();
		}
		
		this.username = username;
	}
	
	
	/*			Mongo Queries*/
	public static User byUsername(String username) {
        return find().field("username").equal(username).get();
    }
	
	
	
}
