package models;

import geometry.Geometry;

import java.util.Date;
import java.util.HashMap;

import org.bson.types.ObjectId;


import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;


import leodagdag.play2morphia.Model;

/**
 * @author Muhammad Fahied
 */

@Entity
public class Feature extends Model

{
	
	@Id
	public String id = new ObjectId().toString();
	public String type = "Feature";
	public HashMap<String, Object> properties;
	
	@Embedded
	public Geometry geometry;
	
	
	
	public static Model.Finder<String, Feature> find()
	{
		
		return new Model.Finder<String, Feature>(String.class, Feature.class);
	
	}
	
	
	public Feature() {
		// TODO Auto-generated constructor stub
	}
	
	public Feature(Geometry geometry) {
		this.geometry = geometry;
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		
	}
	
	public void setProperties(HashMap<String, Object> props){
		this.properties = props;		
	}
	
	
	public HashMap<String, Object> getProperties(){
		return properties;
	}
	
	public void updateProperties(HashMap<String, Object> props){
		this.properties.putAll(props);
	}
	
	
}