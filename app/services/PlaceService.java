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
	 * @param latBL minimal latitude
	 * @param lngBL minimal longitude
	 * @param latTR maximal latitude
	 * @param lngTR maximal longitude
	 * @return all stories in specified area
	 */
	Collection<Place> getPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR);
}
