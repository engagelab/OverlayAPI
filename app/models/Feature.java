package models;

import java.util.HashMap;

import org.bson.types.ObjectId;

import geometry.Geometry;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import leodagdag.play2morphia.Model;



@Entity
public class Feature extends Model

{
	
	@Id
	public String id = new ObjectId().toString();
	public String type = "Feature";
	public HashMap<String, Object> properties;
	public Geometry geometry;
	
	public Feature() {
		// TODO Auto-generated constructor stub
	}
	
	public Feature(Geometry geometry) {
		this.geometry = geometry;
		properties = new HashMap<String, Object>();
	}
	
	public void setProperties(HashMap<String, Object> props){
		this.properties = props;
	}
	
	public HashMap<String, Object> getProperties(){
		return properties;
	}
	
}