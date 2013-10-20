package controllers;

import java.util.Collection;

import models.Place;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.FlickrRetriever;
import services.PlaceService;

public class PlaceController extends Controller {

	public static Result listAllPlaces() {
		PlaceService placeService = new FlickrRetriever();
		Collection<Place> allPlaces = placeService.getAllPlaces();

		return ok(Json.toJson(allPlaces));
	}

	public static Result listPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR) {
		PlaceService placeService = new FlickrRetriever();
		Collection<Place> places = placeService.getPlacesInArea(latBL, lngBL, latTR, lngTR);

		return ok(Json.toJson(places));
	}
}
