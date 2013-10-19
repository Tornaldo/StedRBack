package controllers;

import java.util.Collection;
import java.util.List;

import models.Story;
import models.Place;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.DigitaltFortaltRetriever;
import retrievers.FlickrRetriever;
import services.PlaceService;
import services.StoryService;

public class StoryController extends Controller {
	
	//FIXME instantiate services, got guice?

	public static Result listStoriesForWall(String placeId) {
		StoryService storyService = new DigitaltFortaltRetriever();
		PlaceService placeService = new FlickrRetriever();
		
		Place place = placeService.findPlaceById(placeId);
		
		Collection<Story> stories = storyService.getStoriesForPlace(place);
		
		return ok(Json.toJson(stories));
	}
	
	public static Result listStoriesForWallInRadius(String placeId, Double radius) {
		StoryService storyService = new DigitaltFortaltRetriever();
		PlaceService placeService = new FlickrRetriever();
		
		Place place = placeService.findPlaceById(placeId);
		
		Collection<Story> stories = storyService.getStoriesForPlace(place, radius);
		
		return ok(Json.toJson(stories));
	}
	
}
