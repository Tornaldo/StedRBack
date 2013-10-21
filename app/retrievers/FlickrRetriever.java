package retrievers;

import java.io.IOException;
import java.util.Collection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import models.Place;
import retrievers.flickr.AdditionalDataQuery;
import retrievers.flickr.PhotoQueryForGroup;
import services.PlaceService;
import services.StedrConstants;

public class FlickrRetriever implements PlaceService {

	@Override
	public Collection<Place> getAllPlaces() {
		//TODO caching
		
		try {
			Collection<Place> rawPlaces = new PhotoQueryForGroup(StedrConstants.STEDR_GROUP_ID).getPlaces();
						
			Collection<Place> places = Lists.newArrayList();
			
			// load  additional data (licenses, location)
			for(Place place : rawPlaces) {
				new AdditionalDataQuery(place).load();
				places.add(place);
			}
			
			// filter out those what do not have compatible license
			places = Collections2.filter(places, new Place.HasCompatibleLicense());
			
			// kick out places without a location
			places = Collections2.filter(places, new Place.HasLocation());
			
			// load picture and thumbnail URLs
			for(Place place : places) {
				place.thumbnailUrl = loadPictureUrl(place, "t");
				place.pictureUrl = loadPictureUrl(place, "m");
			}
			
			return places;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Place findPlaceById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Place> getPlacesInRadius(Double lat, Double lng, Double radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Place> getPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String loadPictureUrl(Place place, String picSize) {

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
}
