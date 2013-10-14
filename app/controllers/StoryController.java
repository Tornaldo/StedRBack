package controllers;

import java.util.List;

import models.Story;
import models.WallModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.StoryRetriever;

public class StoryController extends Controller {

	public static Result listStoriesForWall(Long wallId) {
		WallModel wall = WallModel.find.byId(wallId);
		
		List<Story> stories = StoryRetriever.getStoriesOnWall(wall);
		
		return ok(Json.toJson(stories));
	}
	
	public static Result listStoriesForWall(Long wallId, Double radius) {
		WallModel wall = WallModel.find.byId(wallId);
		
		List<Story> stories = StoryRetriever.getStoriesOnWall(wall, radius);
		
		return ok(Json.toJson(stories));
	}
	
}
