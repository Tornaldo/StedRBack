package controllers;

import java.util.Collection;

import models.Story;
import models.Place;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.DigitaltFortaltRetriever;
import retrievers.flickr.AdditionalDataQuery;
import services.StoryService;

public class StoryController extends Controller {
	
	public static Result listStoriesForPlace(String placeId) {
		StoryService storyService = new DigitaltFortaltRetriever();
		
		Place place = new Place();
		place.id = placeId;
		
		new AdditionalDataQuery(place).load();
		
		Collection<Story> stories = storyService.getStoriesForPlace(place);
		
		return ok(Json.toJson(stories));
	}
	
	public static Result listStoriesForPlaceInRadius(String placeId, Double radius) {
		StoryService storyService = new DigitaltFortaltRetriever();
		
		Place place = new Place();
		place.id = placeId;
		
		new AdditionalDataQuery(place).load();
		
		Collection<Story> stories = storyService.getStoriesForPlace(place, radius);
		
		return ok(Json.toJson(stories));
	}
	
}
