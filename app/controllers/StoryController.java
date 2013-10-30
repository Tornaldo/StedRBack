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

/**
 * 
 * @author Simon Stastny
 *
 */
public class StoryController extends Controller {

	/**
	 * Finds all stories associated with this place according to the story server. Works with default radius.
	 * 
	 * @param placeId Id of the place to find the stories for.
	 * @return JSON file with stories
	 */
	public static Result listStoriesForPlace(String placeId) {
		return listStoriesForPlaceInRadius(placeId, null);
	}

	/**
	 * Finds all stories associated with this place according to the story server. Works with explicit radius.
	 * 
	 * @param placeId Id of the place to find the stories for.
	 * @param radius Radius around the place to look for the stories around.
	 * @return JSON file with stories
	 */
	public static Result listStoriesForPlaceInRadius(String placeId, Double radius) {
		StoryService storyService = new DigitaltFortaltRetriever(); // using DigitaltFortalt as a story service

		Place place = new Place();
		place.id = placeId;

		new AdditionalDataQuery(place).load(); // load location data

		Collection<Story> stories;
		
		if (radius != null) {
			// explicit radius
			stories = storyService.getStoriesForPlace(place, radius);
		} else {
			// implicit/default radius
			stories = storyService.getStoriesForPlace(place);
		}

		return ok(Json.toJson(stories));
	}

}
