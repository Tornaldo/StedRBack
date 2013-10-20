package retrievers;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;

import models.Place;

public class FlickrQuery {

	/*
	 * 500 is maximum for one page, if we need more, we need to use paging.
	 * See http://www.flickr.com/services/api/explore/flickr.groups.pools.getPhotos for details
	 */
	private static final int PAGE_SIZE = 500;
	private static final String STEDR_GROUP_ID = "2297124%40N25"; // escaped
	private static final String FLICKR_API_URL = "http://api.flickr.com/services/rest/?method=";

	private FlickrQuery() {
		// private to discourage instantiation
	}

	public static FlickrQuery create() {
		return new FlickrQuery();
	}

	public FlickrPlacesResult call(final String apiKey) throws IOException {
		String methodName = "flickr.groups.pools.getPhotos";

		StringBuffer sb = new StringBuffer();

		sb.append(FLICKR_API_URL);
		sb.append(methodName);
		sb.append("&api_key=");
		sb.append(apiKey);
		sb.append("&group_id=");
		sb.append(STEDR_GROUP_ID);
		sb.append("&format=rest");
		sb.append("&per_page=" + PAGE_SIZE);

		Document doc = Jsoup.connect(sb.toString()).get();

		Elements photos = doc.select("photo");

		return new FlickrPlacesResult(Collections2.transform(photos, new Function<Element, Place>() {

			@Override
			public Place apply(Element element) {
				Long dateAddedUnixTimestamp = Long.valueOf(element.attr("dateadded"));

				Place place = new Place();

				place.id = element.attr("id");
				place.title = element.attr("title");
				place.owner = element.attr("owner");
				place.ownerName = element.attr("ownername");
				place.dateAdded = new Date(TimeUnit.SECONDS.toMillis(dateAddedUnixTimestamp));
				place.thumbnailUrl = loadPictureUrl(place, apiKey, "t");
				place.pictureUrl = loadPictureUrl(place, apiKey, "m");

				loadGeoData(place, apiKey);

				return place;
			}
		}));
	}

	private String loadPictureUrl(Place place, String apiKey, String picSize) {

		StringBuffer sb = new StringBuffer();

		sb.append("http://www.flickr.com/photos/");
		sb.append(place.owner);
		sb.append("/");
		sb.append(place.id);
		sb.append("/sizes/");
		sb.append(picSize);
		sb.append("/in/photostream/");

		Document doc;
		try {
			doc = Jsoup.connect(sb.toString()).get();

			Element image = doc.select("div#allsizes-photo img").get(0);
			return image.attr("src");
				
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void loadGeoData(Place place, String apiKey) {
		String methodName = "flickr.photos.geo.getLocation";

		StringBuffer sb = new StringBuffer();

		sb.append(FLICKR_API_URL);
		sb.append(methodName);
		sb.append("&api_key=");
		sb.append(apiKey);
		sb.append("&&photo_id=");
		sb.append(place.id);
		sb.append("&format=rest");

		Document doc;
		try {
			doc = Jsoup.connect(sb.toString()).get();

			Elements locations = doc.select("location");

			if (!locations.isEmpty()) {
				Element location = locations.get(0);

				place.latitude = Double.valueOf(location.attr("latitude"));
				place.longitude = Double.valueOf(location.attr("longitude"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public class FlickrPlacesResult {

		//TODO implement radius...

		private Collection<Place> places;

		private FlickrPlacesResult(Collection<Place> places) {
			this.places = places;
		}

		public Collection<Place> getPlaces() {
			// return only those that have location data filled in
			return Collections2.filter(places, new Place.HasLocation());
		}

		public FlickrPlacesResult inRadius(Double lng, Double Lat, Double radius) {
			return this;
		}

		public FlickrPlacesResult inArea(final double latBL, final double lngBL, final double latTR, final double lngTR) {
			// filter those that are in selected area
			places = Collections2.filter(places, new Place.IsInArea(latBL, latTR, lngBL, lngTR));

			return this;
		}

		public Place getById(final String id) {
			// return only the place with given id or null
			return FluentIterable.<Place> from(places).filter(new Place.HasId(id)).first().orNull();
		}
	}
}
