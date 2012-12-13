package data;


public class BasicImage {
	
	public String url;
	public int width;
	public int height;
	
	public BasicImage() {
		
	}
	
	public BasicImage(String url, int width, int height) {
		
		this.url = url;
		this.width = width;
		this.height = height;
	}
	
public BasicImage(String url) {
		
		this.url = url;
		this.width = 0;
		this.height = 0;
	}


}

