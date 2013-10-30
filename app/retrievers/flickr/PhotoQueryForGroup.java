package retrievers.flickr;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import models.Place;

public class PhotoQueryForGroup extends FlickrQuery {
	
	/*
	 * IMPROVE
	 * 500 is maximum for one page, if we need more, we need to use paging.
	 * See http://www.flickr.com/services/api/explore/flickr.groups.pools.getPhotos for details
	 */
	private static final int PAGE_SIZE = 500;
	
	private final String groupId;
	
	public PhotoQueryForGroup(String groupId) {
		super();
		this.groupId = groupId;
	}
	
	public Collection<Place> getPlaces() throws IOException {
		Document doc = Jsoup.connect(makeRequestUrl()).get();
		Elements photoElements = doc.select("photo");
		
		// returns elements mapped onto places
		return Collections2.transform(photoElements, photoElementToPlaceMapping);
	}
	
	@Override
	protected String getMethodName() {
		return "flickr.groups.pools.getPhotos";
	}

	@Override
	protected void setAdditionalParams() {
		addParameter("group_id", groupId);
		addParameter("per_page", PAGE_SIZE);
	}
	
	private static Function<Element, Place> photoElementToPlaceMapping = new Function<Element, Place>() {
		@Override
		public Place apply(Element element) {
			Long dateAddedUnixTimestamp = Long.valueOf(element.attr("dateadded"));

			Place place = new Place();
			place.id = element.attr("id");
			place.title = element.attr("title");
			place.owner = element.attr("owner");
			place.ownerName = element.attr("ownername");
			place.dateAdded = new Date(TimeUnit.SECONDS.toMillis(dateAddedUnixTimestamp));
			
			return place;
		}
	};
}
