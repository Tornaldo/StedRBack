package services;

import java.util.Collection;

import models.Place;

/**
 * @author Simon Stastny
 *
 */
public interface PlaceService {
	
	/**
	 * @return all places
	 */
	Collection<Place> getAllPlaces();
	
	/**
	 * @param id
	 * @return one place with specified id or null
	 */
	Place findPlaceById(String id);
	
	/**
	 * @param lat
	 * @param lng
	 * @param radius
	 * @return all places what are in the radius of the specified point
	 */
	Collection<Place> getPlacesInRadius(Double lat, Double lng, Double radius);
	
	/**
	 * @param latBL minimal latitude
	 * @param lngBL minimal longitude
	 * @param latTR maximal latitude
	 * @param lngTR maximal longitude
	 * @return all stories in specified area
	 */
	Collection<Place> getPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR);
}
