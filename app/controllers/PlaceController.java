package controllers;

import java.util.Collection;

import com.google.common.collect.Lists;

import models.Place;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.FlickrRetriever;
import services.PlaceService;

/**
 * 
 * @author Simon Stastny
 *
 */
public class PlaceController extends Controller {

	/**
	 * Returns all the places in the PlaceService used.
	 * 
	 * @return JSON file with places
	 */
	public static Result listAllPlaces() {
		PlaceService placeService = new FlickrRetriever(); // using flickr as the place service
		Collection<Place> allPlaces = placeService.getAllPlaces();

		if(allPlaces == null) {
			allPlaces = Lists.newArrayList();
		}
		
		return ok(Json.toJson(allPlaces));
	}

	/**
	 * Finds all the places in a rectangle area defined by bottom-left and top-right corner on a map.
	 * 
	 * @param latBL minimal latitude
	 * @param lngBL minimal longitude
	 * @param latTR maximal latitude
	 * @param lngTR maximal longitude
	 * @return JSON file with places
	 */
	public static Result listPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR) {
		PlaceService placeService = new FlickrRetriever(); // using flickr as the place service
		Collection<Place> places = placeService.getPlacesInArea(latBL, lngBL, latTR, lngTR);

		return ok(Json.toJson(places));
	}
}
