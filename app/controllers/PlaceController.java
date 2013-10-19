package controllers;

import java.util.Collection;
import java.util.List;

import models.Place;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.FlickrRetriever;
import services.PlaceService;

public class PlaceController extends Controller {

	public static Result listAllPlaces() {
		PlaceService placeService = new FlickrRetriever(); // FIXME got guice?
		Collection<Place> allPlaces = placeService.getAllPlaces();

		return ok(Json.toJson(allPlaces));
	}

	public static Result listPlacesInArea(Double startLat, Double startLong, Double stopLat, Double stopLong) {
		PlaceService placeService = new FlickrRetriever(); // FIXME got guice?
		Collection<Place> placesInArea = placeService.getPlacesInArea(startLong, startLat, stopLong, stopLat);

		return ok(Json.toJson(placesInArea));
	}
}
