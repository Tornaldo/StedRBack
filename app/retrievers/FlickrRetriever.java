package retrievers;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import models.Place;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import retrievers.flickr.AdditionalDataQuery;
import retrievers.flickr.PhotoQueryForGroup;
import services.PlaceService;
import services.StedrConstants;

import com.google.common.base.Stopwatch;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class FlickrRetriever implements PlaceService {
	
	private static Cache<String, Place> placeCache = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.HOURS)
			.maximumSize(2000)
			.build();
	
	@Override
	public Collection<Place> getAllPlaces() {
		try {
			Collection<Place> rawPlaces = new PhotoQueryForGroup(StedrConstants.STEDR_GROUP_ID).getPlaces();
						
			Collection<Place> places = Lists.newArrayList();
			
			// load  additional data (licenses, location)
			for(Place place : rawPlaces) {
				places.add(placeCache.get(place.id, new PlaceLoader(place)));
			}

			// filter out those what do not have compatible license
			places = Collections2.filter(places, new Place.HasCompatibleLicense());
			
			// kick out places without a location
			places = Collections2.filter(places, new Place.HasLocation());
			
			//FIXME check license and location before extracting pics
			
			return places;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
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
	
	private static String loadPictureUrl(Place place, String picSize) {

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
	
	static class PlaceLoader implements Callable<Place>{
		
		private final Place place;

		public PlaceLoader(Place place) {
			super();
			this.place = place;
		}

		@Override
		public Place call() throws Exception {
			// load license and location
			new AdditionalDataQuery(place).load();
			
			// load pics only if this place is actually valid (costly operation)
			if (place.hasCompatibleLicense() && place.hasLocation()) {
				// FIXME remove stopwatch
				Stopwatch stopwatch = new Stopwatch();
				stopwatch.start();
				place.pictureUrl = loadPictureUrl(place, "m");
				place.thumbnailUrl = loadPictureUrl(place, "t");
				stopwatch.stop();
				System.out.println("pics: " + stopwatch);
			}

			return place;
		}
		
	}
}
