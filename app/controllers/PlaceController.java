package controllers;

import java.util.Collection;

import com.google.common.collect.Lists;

import models.Place;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.FlickrRetriever;
import services.PlaceService;

public class PlaceController extends Controller {

	/**
	 * @return JSON file with all the places listed.
	 */
	public static Result listAllPlaces() {
		PlaceService placeService = new FlickrRetriever();
		Collection<Place> allPlaces = placeService.getAllPlaces();

		if(allPlaces == null) {
			allPlaces = Lists.newArrayList();
		}
		
		return ok(Json.toJson(allPlaces));
	}

	/**
	 * @return JSON file with all the places in a rectangle area defined by bottom-left and top-right corner on a map.
	 */
	public static Result listPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR) {
		PlaceService placeService = new FlickrRetriever();
		Collection<Place> places = placeService.getPlacesInArea(latBL, lngBL, latTR, lngTR);

		return ok(Json.toJson(places));
	}
}
