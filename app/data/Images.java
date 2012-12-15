package data;

import com.google.code.morphia.annotations.Embedded;

import external.Constants;

@Embedded
public class Images {
	
	public BasicImage low_resolution;
	public BasicImage thumbnail;
	public BasicImage standard_resolution;
	
	public Images() {
		
	}
	
	public Images(String standard_resolution) {
		
		String url = Constants.SERVER_NAME+"/image/"+standard_resolution;
		this.standard_resolution = new BasicImage(url, 612, 612);
		this.low_resolution = new BasicImage();
		this.thumbnail = new BasicImage();
		
	}
	
	
	
	public Images(BasicImage low_resolution, BasicImage thumbnail, BasicImage standard_resolution) 
	{
		this.low_resolution = low_resolution;
		this.thumbnail = thumbnail;
		this.standard_resolution = standard_resolution;
	}

}
