package retrievers;

import java.io.IOException;
import java.util.Collection;

import models.Place;
import services.PlaceService;

public class FlickrRetriever implements PlaceService {

	// this apikey belongs to: chrisfro@stud.ntnu.no
	public static final String FLICKR_API_KEY = "cd04f142470e7de7c992b3a3b140f636";

	@Override
	public Collection<Place> getAllPlaces() {
		try {
			return FlickrQuery.create().call(FLICKR_API_KEY).getPlaces();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Place findPlaceById(String id) {
		try {
			return FlickrQuery.create().call(FLICKR_API_KEY).getById(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Collection<Place> getPlacesInRadius(Double lat, Double lng, Double radius) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Place> getPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR) {
		try {
			return FlickrQuery.create().call(FLICKR_API_KEY).inArea(latBL, lngBL, latTR, lngTR).getPlaces();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
