package models;

import org.bson.types.ObjectId;

import geometry.Geometry;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import leodagdag.play2morphia.Model;



@Entity
public class Feature extends Model{
	
	@Id
	public String id = new ObjectId().toString();
	public String type = "Feature";
	public Object properties;
	public Geometry geometry;

	public Feature(Geometry geometry) {
		this.geometry = geometry;
	}
	
}