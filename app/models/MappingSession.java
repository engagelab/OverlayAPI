package models;

import geometry.Geometry;
import geometry.Point;

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
public class MappingSession extends Model
{


	@Id
	public String id = new ObjectId().toString();
	
	//title is more description then using the name
	public String title;
	
	public String contextual_info;
	
	public double []boundingBox;
	
	public int nPOIs;
	
	public String facebook_group_name;
	
	@Reference
	public List<Feature> features;
	
	
	public static Model.Finder<String, MappingSession> find()
	{
		
		return new Model.Finder<String, MappingSession>(String.class, MappingSession.class);
	
	}
	
	public MappingSession() {
		// TODO Auto-generated constructor stub
	}
	

	public MappingSession(String title, String contexual_info, double[] boundingBox, int nPOIs, String facebook_group_name) {
		this.title = title;
		this.contextual_info = contexual_info;
		this.boundingBox=boundingBox;
		this.nPOIs=nPOIs;
		this.facebook_group_name=facebook_group_name;
		if(features == null)
		{
			features = new ArrayList<Feature>();
			features = createRandomPOIsWithinBoundingBox(boundingBox, nPOIs);
		}
	}
	
	
	/* generate random number between the range
	 * Min + (int)(Math.random() * ((Max - Min) + 1))
	 * */
	private  List<Feature> createRandomPOIsWithinBoundingBox(double[] boundingBox, int nPOIs) 
	{
		
		double lat1 = boundingBox[0];
		double lng1 = boundingBox[1];
		double lat2 = boundingBox[2];
		double lng2 = boundingBox[3];
		
		List<Feature> features = new ArrayList<Feature>();
		
		    for (int idx = 1; idx <= nPOIs; ++idx)
		    {
		    	double rlat = lat1 + (Math.random() * ((lat2 - lat1) + 1));
		    	double rlng = lng1 + (Math.random() * ((lng2 - lng1) + 1));
		    	Geometry geometry = new Point(rlng, rlat);
		    	Feature feature = new Feature(geometry);
		    	features.add(feature);
		    }
        return features;
    }
	
	

	/*				MongoDB Queries				*/
	
	public static MappingSession byTitle(String title) {
        return find().field("title").equal(title).get();
    }
	
	public static MappingSession byGroup(String facebook_group_name) {
        return find().field("facebook_group_name").equal(facebook_group_name).get();
    }
	
	
	
	
}
