package services;

import java.util.Collection;

import models.Place;

/**
 * @author Simon Stastny
 * 
 */
public interface PlaceService {

	/**
	 * Returns all the places in the PlaceService.
	 * 
	 * @return Collection of places.
	 */
	Collection<Place> getAllPlaces();

	/**
	 * Returns all stories in the specified area.
	 * 
	 * @param latBL minimal latitude
	 * @param lngBL minimal longitude
	 * @param latTR maximal latitude
	 * @param lngTR maximal longitude
	 * @return Collection of stories.
	 */
	Collection<Place> getPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR);
}
