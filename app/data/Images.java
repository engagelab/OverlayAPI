package data;

public class Images {
	
	public String low_resolution;
	public String thumbnail;
	public String standard_resolution;
	
	public Images() {
		
	}
	
	public Images(String standard_resolution) {
		this.standard_resolution = standard_resolution;
		
	}
	
	
	
	public Images(String low_resolution, String thumbnail, String standard_resolution) 
	{
		this.low_resolution = low_resolution;
		this.thumbnail = thumbnail;
		this.standard_resolution = standard_resolution;
	}

}
