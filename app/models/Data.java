package models;

import java.util.Set;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

import data.Images;

import leodagdag.play2morphia.Model;

@Entity
public class Data extends Model{

	@Id
	public String id = new ObjectId().toString();
	
	public String type;
	
	public Set<String> tags;
	
	public String caption;
	
	@Indexed((IndexDirection.GEO2D))
	public Double [] location;
	
	public long created_time;
	
	public Images images;
	
	public Data() {
		
	}
	
	
	
}
