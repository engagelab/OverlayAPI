package models;

import geometry.Geometry;
import geometry.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;

import leodagdag.play2morphia.Model;
/**
 * @author Muhammad Fahied
 */

@Entity
public class MappingSession extends Model
{


	@Id
	public String id = new ObjectId().toString();
	
	public HashMap<String, Object> properties;
	
	@Reference
	public List<Feature> features;
	
	
	public static Model.Finder<String, MappingSession> find()
	{
		
		return new Model.Finder<String, MappingSession>(String.class, MappingSession.class);
	
	}
	

	public MappingSession() {

		if(features == null)
		{
			features = new ArrayList<Feature>();
			//features = createRandomPOIsWithinBoundingBox(boundingBox, nPOIs);
		}
	}
	
	
	/* generate random number between the range
	 * Min + (int)(Math.random() * ((Max - Min) + 1))
	 * 
	 * double random = new Random().nextDouble();
		double result = start + (random * (end - start));
	 * */
	public  List<Feature> createRandomPOIsWithinBoundingBox(double[] boundingBox, int nPOIs) 
	{
		
		double lng1 = boundingBox[0];
		double lat1 = boundingBox[1];
		double lng2 = boundingBox[2];
		double lat2 = boundingBox[3];
		
		List<Feature> features = new ArrayList<Feature>();
		
		    for (int idx = 1; idx <= nPOIs; ++idx)
		    {
		    	double random = new Random().nextDouble();
		    	double rlat = lat1 + (random * ((lat2 - lat1)));
		    	double rlng = lng1 + (random * ((lng2 - lng1)));
		    	Geometry geometry = new Point(rlng, rlat);
		    	Feature feature = new Feature(geometry);
		    	feature.properties.put("source_type", "empty");
		    	feature.properties.put("name", "empty");
		    	features.add(feature);
		    	feature.insert();
		    }
        return features;
    }
	
}
