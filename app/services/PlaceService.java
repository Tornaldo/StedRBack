package services;

import java.util.Collection;

import models.Place;

public interface PlaceService {
	
	public Collection<Place> getAllPlaces();
	
	public Place findPlaceById(String id);
	
	public Collection<Place> getPlacesInRadius(Double lng, Double lat, Double radius);
	
	public Collection<Place> getPlacesInArea(Double lngBL, Double latBL, Double lngTR, Double latTR);

}
