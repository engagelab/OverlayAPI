package helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.StrBuilder;

public class TwitterHelper {
	
	
	public static String parse(String tweetText) {

	     // Search for URLs
//	     if (tweetText && tweetText.contains('http:')) 
//	     {
//	         int indexOfHttp = tweetText.indexOf('http:');
//	         int endPoint = (tweetText.indexOf(' ', indexOfHttp) != -1) ? 
//	        		 tweetText.indexOf(' ', indexOfHttp) : tweetText.length();
//	         String url = tweetText.substring(indexOfHttp, endPoint);
//	         String targetUrlHtml=  "<a href='${url}' target='_blank'>${url}</a>";
//	         tweetText = tweetText.replace(url,targetUrlHtml );
//	     }

	     String patternStr = "(?:\\s|\\A)[##]+([A-Za-z0-9-_]+)";
	     Pattern pattern = Pattern.compile(patternStr);
	     Matcher matcher = pattern.matcher(tweetText);
	     String result = "";

	     // Search for Hashtags
	     while (matcher.find()) {
	         result = matcher.group();
	         result = result.replace(" ", "");
	         String search = result.replace("#", "");
	         String searchHTML="<a href='http://search.twitter.com/search?q=" + search + "'>" + result + "</a>";
	         tweetText = tweetText.replace(result,searchHTML);
	     }

	     // Search for Users
	     patternStr = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
	     pattern = Pattern.compile(patternStr);
	     matcher = pattern.matcher(tweetText);
	     while (matcher.find()) {
	         result = matcher.group();
	         result = result.replace(" ", "");
	         String rawName = result.replace("@", "");
	         String userHTML="<a href='http://twitter.com/${rawName}'>" + result + "</a>";
	         tweetText = tweetText.replace(result,userHTML);
	     }
	     return tweetText;
	 }
	
	
	@SuppressWarnings("null")
	public static List<String> searchHashTags(String tweetText)
	{
		 String patternStr = "(?:\\s|\\A)[##]+([A-Za-z0-9-_]+)";
	     Pattern pattern = Pattern.compile(patternStr);
	     Matcher matcher = pattern.matcher(tweetText);
	     
	     
	     String  result = "";
	     List<String>hashTags = new ArrayList<String>();

	     // Search for Hashtags
	     int i = 0;
	     while (matcher.find()) {
	    	 result = matcher.group();
	         result = result.replace(" ", "");
	         String search = result.replace("#", "");
	         hashTags.add(search);
	     }
	     
		return hashTags;
		
	}


}
