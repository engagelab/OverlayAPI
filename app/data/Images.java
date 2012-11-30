package data;

public class Images {
	
	public Image low_resolution;
	public Image thumbnail;
	public Image standard_resolution;
	
	public Images() {
		
	}
	
	public Images(Image low_resolution, Image thumbnail, Image standard_resolution) 
	{
		this.low_resolution = low_resolution;
		this.thumbnail = thumbnail;
		this.standard_resolution = standard_resolution;
	}

}
