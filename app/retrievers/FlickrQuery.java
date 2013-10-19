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

	private static final String FLICKR_API_URL = "http://api.flickr.com/services/rest/?method=";

	// FIXME implements taggs

	private FlickrQuery() {
		// private to discourage instantiation
	}

	public static FlickrQuery create() {
		return new FlickrQuery();
	}

	public FlickrQueryResult call(final String apiKey) throws IOException {
		String methodName = "flickr.groups.pools.getPhotos";

		StringBuffer sb = new StringBuffer();

		sb.append(FLICKR_API_URL);
		sb.append(methodName);
		sb.append("&api_key=");
		sb.append(apiKey);
		sb.append("&group_id=");
		sb.append("2297124%40N25"); // FIXME externalize
		sb.append("&format=rest");

		Document doc = Jsoup.connect(sb.toString()).get();

		Elements photos = doc.select("photo");

		return new FlickrQueryResult(Collections2.transform(photos, new Function<Element, Place>() {

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
		sb.append(picSize); // FIXME size externalize
		sb.append("/in/photostream/");

		Document doc;
		try {
			doc = Jsoup.connect(sb.toString()).get();

			Element image = doc.select("div#allsizes-photo img:first-child").get(0);
			return image.attr("src");
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private void loadGeoData(Place place, String apiKey) {
		String methodName = "flickr.photos.geo.getLocation";

		StringBuffer sb = new StringBuffer();

		sb.append(FLICKR_API_URL);
		sb.append(methodName);
		sb.append("&api_key=");
		sb.append(apiKey);
		sb.append("&&photo_id=");
		sb.append(place.id); // FIXME externalize
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public class FlickrQueryResult {

		// FIXME implement radius...

		private Collection<Place> places;

		private FlickrQueryResult(Collection<Place> places) {
			this.places = places;
		}

		public Collection<Place> getPlaces() {
			return places;
		}

		public FlickrQueryResult inRadius(Double lng, Double Lat, Double radius) {
			return this;
		}

		public FlickrQueryResult inArea(final double lngBL, final double latBL, final double lngTR, final double latTR) {
			places = Collections2.filter(places, new Predicate<Place>() {

				@Override
				public boolean apply(Place place) {

					if (place.latitude < latBL || place.latitude > latTR) {
						return false;
					}

					if (place.longitude < lngBL || place.longitude > lngTR) {
						return false;
					}

					return true;
				}

			});

			return this;
		}

		public Place getById(final String id) {
			return FluentIterable.<Place> from(places).filter(new Predicate<Place>() {
				@Override
				public boolean apply(Place place) {
					return place.id.equals(id);
				}
			}).first().orNull();
		}

	}

}
