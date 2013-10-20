package retrievers;

import java.io.IOException;
import java.util.Collection;

import models.Place;
import services.PlaceService;
import services.StedrConstrants;

public class FlickrRetriever implements PlaceService {

	@Override
	public Collection<Place> getAllPlaces() {
		try {
			return FlickrQuery.create().call(StedrConstrants.FLICKR_API_KEY).getPlaces();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Place findPlaceById(String id) {
		try {
			return FlickrQuery.create().call(StedrConstrants.FLICKR_API_KEY).getById(id);
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
			return FlickrQuery.create().call(StedrConstrants.FLICKR_API_KEY).inArea(latBL, lngBL, latTR, lngTR).getPlaces();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
