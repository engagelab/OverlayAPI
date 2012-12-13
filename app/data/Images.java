package data;

import external.Constants;

public class Images {
	
	public Image low_resolution;
	public Image thumbnail;
	public Image standard_resolution;
	
	public Images() {
		
	}
	
	public Images(String standard_resolution) {
		
		String url = Constants.SERVER_NAME+"/image/"+standard_resolution;
		this.standard_resolution = new Image(url, 612, 612);
		this.low_resolution = new Image();
		this.thumbnail = new Image();
		
	}
	
	
	
	public Images(Image low_resolution, Image thumbnail, Image standard_resolution) 
	{
		this.low_resolution = low_resolution;
		this.thumbnail = thumbnail;
		this.standard_resolution = standard_resolution;
	}

}
