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
public class HashTagTable extends Model {
	
	@Id
	public String id = new ObjectId().toString();
	
	public String hashTag;
	
	@Reference(ignoreMissing = true)
	public List<Feature> features;
	
	public static Model.Finder<String, HashTagTable> find()
	{
		
		return new Model.Finder<String, HashTagTable>(String.class, HashTagTable.class);
	
	}
	
	
	public HashTagTable() {
		// TODO Auto-generated constructor stub
	}
	
	public HashTagTable(String hashTag) {
		
		if (features == null) {
			features = new ArrayList<Feature>();
		}
		
		this.hashTag = hashTag;
	}
	
	
	
	public static HashTagTable byTag(String hashTag) {
        return find().field("hashTag").equal(hashTag).get();
    }

}
