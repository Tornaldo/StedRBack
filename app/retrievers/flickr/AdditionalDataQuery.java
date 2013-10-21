package retrievers.flickr;

import java.io.IOException;

import models.Place;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AdditionalDataQuery extends FlickrQuery {

	private Place place;

	public AdditionalDataQuery(Place place) {
		super();
		this.place = place;
	}

	@Override
	protected String getMethodName() {
		return "flickr.photos.getInfo";
	}

	@Override
	protected void setAdditionalParams() {
		addParameter("photo_id", place.id);
	}

	public void load() {
		Document doc;
		try {
			doc = Jsoup.connect(makeRequestUrl()).get();

			Elements photos = doc.select("photo");

			if (!photos.isEmpty()) {
				Element photo = photos.get(0);
				place.license = Integer.valueOf(photo.attr("license"));
			}

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

}
