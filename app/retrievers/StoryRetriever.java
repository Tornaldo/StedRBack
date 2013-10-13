package retrievers;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import scala.Array;
import models.Picture;
import models.Story;
import models.WallModel;

public class StoryRetriever {
	
	public static List<Story> getStoriesOnWall(WallModel wall) {
		try {
			List<Story> stories = new ArrayList<Story>();
			
			Document doc = Jsoup.connect(""
	                + "http://kulturnett2.delving.org/organizations/kulturnett/api/search?"
	                + "query=*:*&" 
	                + "pt="
	                + wall.latitude //FIXME NPE
	                + ","
	                + wall.longitude //FIXME NPE
	                + "&" 
	                + "d=1&" //FIXME const
	                + "format=xml&"
	                + "rows=500&" 
	                + "qf=abm_contentProvider_facet:Digitalt+fortalt")
	                .get();
	        
	        Elements items = doc.select("item");
	        
	        for(Element item : items) {
	        	
	        	Story story = new Story();
	            List<String> pictures = new ArrayList<String>();
	            
	            Element fields = item.select("fields").first();
	            
	            for(Element field : fields.getAllElements()) {
	                System.out.println(field.tagName());
	                
	                
	                
	                if(field.tagName().equalsIgnoreCase("abm:imageUri") || field.tagName().equalsIgnoreCase("abm:videoUri")) {
	                	pictures.add(field.ownText());
	                }
	                
	                if(field.tagName().equals("dc:title")) {
	                	story.setTitle(field.ownText());
	                }
	            }
	            
	            if(!pictures.isEmpty()) {
	            	story.setPictures(pictures);
	            }
	            
	            stories.add(story);
	        }
	        
	        return stories;
			
			
		} catch (Exception e) {
			// FIXME
		}
		
		return null;
	}

}
