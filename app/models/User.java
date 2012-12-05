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
	
	public String facebook_id;
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
	

	
	public User(String facebook_id, String full_name) {
		
		if (features == null) {
			features = new ArrayList<Feature>();
		}
		
		this.facebook_id = facebook_id;
	}
	
	
	/*			Mongo Queries*/
	public static User byfacebook_id(String facebook_id) {
        return find().field("facebook_id").equal(facebook_id).get();
    }
	
	
	
}
