package services;

import java.util.Collection;

import models.Place;

public interface PlaceService {
	
	Collection<Place> getAllPlaces();
	
	Place findPlaceById(String id);
	
	Collection<Place> getPlacesInRadius(Double lat, Double lng, Double radius);
	
	Collection<Place> getPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR);

}
