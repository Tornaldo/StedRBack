package retrievers;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import models.Story;
import models.WallModel;

public class StoryRetriever {

	private static final double DEFAULT_RADIUS = 0.1d;
	private static final int DEFAULT_ROWS = 500;

	public static List<Story> getStoriesOnWall(WallModel wall) {
		return getStoriesOnWall(wall, DEFAULT_RADIUS);
	}

	public static List<Story> getStoriesOnWall(WallModel wall, Double radius) {
		if (wall == null || radius < 0) {
			return null;
		}

		try {
			List<Story> stories = new ArrayList<Story>();

			Document doc = Jsoup.connect("" + 
					"http://kulturnett2.delving.org/organizations/kulturnett/api/search?" + 
					"query=*:*&" + 
					"pt=" + wall.latitude + "," + wall.longitude + "&" + 
					"d=" + radius + "&" + 
					"format=xml&" + 
					"rows=" + DEFAULT_ROWS + "&" + 
					"qf=abm_contentProvider_facet:Digitalt+fortalt")
					.get();

			Elements items = doc.select("item");

			// each element is a story
			for (Element item : items) {
				
				Story story = new Story();
				List<String> pictures = new ArrayList<String>();
				List<String> creators = new ArrayList<String>();
				List<String> tags = new ArrayList<String>();

				Element fields = item.select("fields").first();
				for (Element field : fields.getAllElements()) {

					// save pictures
					if (field.tagName().equalsIgnoreCase("abm:imageUri") ||
							field.tagName().equalsIgnoreCase("abm:videoUri")) {
						pictures.add(field.ownText());
					}
					
					// save creators/authors
					if (field.tagName().equalsIgnoreCase("dc:creator")) {
						creators.add(field.ownText());
					}

					// save tags
					if (field.tagName().equalsIgnoreCase("dc:subject")) {
						tags.add(field.ownText());
					}
					
					// save title
					if (field.tagName().equals("dc:title")) {
						story.setTitle(field.ownText());
					}
					
					// save category
					if (field.tagName().equals("abm:category")) {
						story.setCategory(field.ownText());
					}
				}

				// found any pictures?
				if (!pictures.isEmpty()) {
					story.setPictures(pictures);
				}
				
				// found any pictures?
				if (!creators.isEmpty()) {
					story.setCreators(creators);
				}
				
				// found any pictures?
				if (!tags.isEmpty()) {
					story.setTags(tags);
				}

				stories.add(story);
			}

			return stories;

		} catch (Exception e) {
			//FIXME logging
		}

		return null;
	}

}
