/*
 *	Copyright (c) 2013, Odd Fredrik Rogstad, Christian Frøystad, Simon Stastny, Knut Nergård
 *	All rights reserved.
 *	
 *	Redistribution and use in source and binary forms, with or without
 *	modification, are permitted provided that the following conditions are met:
 *	* Redistributions of source code must retain the above copyright
 *	  notice, this list of conditions and the following disclaimer.
 *	* Redistributions in binary form must reproduce the above copyright
 *	  notice, this list of conditions and the following disclaimer in the
 *	  documentation and/or other materials provided with the distribution.
 *	* Neither the name of the project nor the 
 *	  names of its contributors may be used to endorse or promote products
 *	  derived from this software without specific prior written permission.
 *	
 *	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNERS BE LIABLE FOR ANY
 *	DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package retrievers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import models.Place;
import models.Story;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import services.StoryService;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * Implementation of StoryService using DigitaltFortalt.
 * 
 * @author Simon Stastny
 * 
 */
public class DigitaltFortaltRetriever implements StoryService {

	private static final double DEFAULT_RADIUS = 0.06d; // in kilometres
	private static final int DEFAULT_ROWS = 500;

	public Collection<Story> getStoriesForPlace(Place place, Double radius) {
		if (place == null ||
			place.longitude == null || 
			place.latitude == null || 
			radius < 0) {
			return null;
		}

		try {
			// build request url to query DF
			StringBuffer sb = new StringBuffer();
			sb.append("http://kulturnett2.delving.org/organizations/kulturnett/api/search?");
			sb.append("query=*:*&");
			sb.append("pt=").append(place.latitude).append(",").append(place.longitude).append("&");
			sb.append("d=").append(radius).append("&");
			sb.append("format=xml&");
			sb.append("rows=").append(DEFAULT_ROWS).append("&");;
			sb.append("qf=abm_contentProvider_facet:Digitalt+fortalt");
			
			// get the response
			Document doc = Jsoup.connect(sb.toString()).get();

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
			story.ingress = "";

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
					story.ingress = story.ingress + field.ownText() + " ";
				}

				// save fortelling
				if (field.tagName().equals("dc:description")) {
					String raw = field.ownText();
					
					String clean = raw.replace("</p>", "\n</p>").replaceAll("\\<[^>]*>","");
					
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
