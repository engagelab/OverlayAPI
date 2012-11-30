package geometry;

import com.google.code.morphia.annotations.Embedded;

/**
 * @author Muhammad Fahied
 */
@Embedded
public class Geometry {
	
	private String type;
	
	public Geometry() {
		
	}
	public Geometry(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
