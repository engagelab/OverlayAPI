package geometry;

import java.util.List;


/**
 * @author Muhammad Fahied
 */

public class GeometryCollection extends Geometry {
	
	private List<Geometry> geometry;
	
	public GeometryCollection(List<Geometry> geometry) {
		super(GeometryCollection.class.getSimpleName());
		this.geometry = geometry;
	}
	
	public List<Geometry> getGeometries() {
		return geometry;
	}
}
