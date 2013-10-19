package controllers;

import java.util.List;

import models.Story;
import models.WallModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.DigitaltFortaltRetriever;

public class StoryController extends Controller {

	public static Result listStoriesForWall(Long wallId) {
		WallModel wall = WallModel.find.byId(wallId);
		
		List<Story> stories = DigitaltFortaltRetriever.getStoriesOnWall(wall);
		
		return ok(Json.toJson(stories));
	}
	
	public static Result listStoriesForWallInRadius(Long wallId, Double radius) {
		WallModel wall = WallModel.find.byId(wallId);
		
		List<Story> stories = DigitaltFortaltRetriever.getStoriesOnWall(wall, radius);
		
		return ok(Json.toJson(stories));
	}
	
}
