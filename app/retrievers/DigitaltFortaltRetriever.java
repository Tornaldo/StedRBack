package retrievers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import services.StoryService;
import models.Story;
import models.Place;

/**
 * Implementation of StoryService using DigitaltFortalt.
 * 
 * @author Simon Stastny
 * 
 */
public class DigitaltFortaltRetriever implements StoryService {

	private static final double DEFAULT_RADIUS = 0.1d; // in kilometres
	private static final int DEFAULT_ROWS = 500;

	public Collection<Story> getStoriesForPlace(Place place, Double radius) {
		if (place == null ||
			place.longitude == null || 
			place.latitude == null || 
			radius < 0) {
			return null;
		}

		try {
			// build request and get the response
			Document doc = Jsoup.connect("" 
					+ "http://kulturnett2.delving.org/organizations/kulturnett/api/search?" 
					+ "query=*:*&" 
					+ "pt=" + place.latitude + "," + place.longitude + "&" 
					+ "d=" + radius + "&" 
					+ "format=xml&" 
					+ "rows=" + DEFAULT_ROWS + "&" 
					+ "qf=abm_contentProvider_facet:Digitalt+fortalt").get();

			// find items
			Elements storyItems = doc.select("item");

			// return items mapped to stories
			return Collections2.transform(storyItems, jsonElementToStoryMapping);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Collection<Story> getStoriesForPlace(Place place) {
		return getStoriesForPlace(place, DEFAULT_RADIUS);
	}
	
	private static Function<Element, Story> jsonElementToStoryMapping = new Function<Element, Story>() {
		@Override
		public Story apply(Element item) {
			Story story = new Story();
			List<String> pictures = new ArrayList<String>();
			List<String> videos = new ArrayList<String>();
			List<String> tags = new ArrayList<String>();

			Element fields = item.select("fields").first();
			for (Element field : fields.getAllElements()) {

				// story iself

				// save title
				if (field.tagName().equals("dc:title")) {
					story.title = field.ownText();
				}

				// save ingress
				if (field.tagName().equals("abm:introduction")) {
					story.ingress = story.title + field.ownText() + " ";
				}

				// save fortelling
				if (field.tagName().equals("dc:description")) {
					String raw = field.ownText();
					
					String clean = raw.replace("<p>", "\n<p>").replaceAll("\\<[^>]*>","");
					
					// this strips down HTML but also new lines: String clean = Jsoup.parse(raw).text();
					story.fortelling = clean;
				}

				// pictures and videos

				// save pictures
				if (field.tagName().equalsIgnoreCase("abm:imageUri")) {
					pictures.add(field.ownText());
				}

				// save videos
				if (field.tagName().equalsIgnoreCase("abm:videoUri")) {
					videos.add(field.ownText());
				}

				// creator and institution

				// save author
				// taking the first creator only
				if (field.tagName().equalsIgnoreCase("dc:creator") && 
					story.author == null) {
					
					story.author = field.ownText();
				}

				// save institution
				// taking the second or other creator
				if (field.tagName().equalsIgnoreCase("dc:creator") && 
					story.author != null) {
					
					story.author = field.ownText();
				}

				// filtering attributes

				// save tags
				if (field.tagName().equalsIgnoreCase("dc:subject")) {
					tags.add(field.ownText());
				}

				// save language
				if (field.tagName().equalsIgnoreCase("dc:language")) {
					story.language = field.ownText();
				}

				// save category
				if (field.tagName().equals("abm:category")) {
					story.category = field.ownText();
				}
			}

			// found any pictures?
			if (!pictures.isEmpty()) {
				story.pictures = pictures;
			}

			// found any videos?
			if (!videos.isEmpty()) {
				story.videos = videos;
			}

			// found any pictures?
			if (!tags.isEmpty()) {
				story.tags = tags;
			}
			
			return story;
		}
	};

}
