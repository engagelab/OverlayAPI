package models;

import java.util.ArrayList;
import java.util.List;

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
	public String id;
	
	public String full_name;
	
	public String profile_picture;
	
	@Reference(ignoreMissing = true)
	public List<Feature> features;
	
	
	public static Model.Finder<String, User> find()
	{
		
		return new Model.Finder<String, User>(String.class, User.class);
	
	}
	
	
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	

	
	public User(String id, String full_name) {
		
		if (features == null) {
			features = new ArrayList<Feature>();
		}
		
		this.id = id;
		this.full_name = full_name;
	}
	
	public User(String id, String full_name, String profile_picture) {
		
		if (features == null) {
			features = new ArrayList<Feature>();
		}
		
		this.id = id;
		this.full_name = full_name;
		this.profile_picture = profile_picture;
	}
	
	

	
	
	
}
