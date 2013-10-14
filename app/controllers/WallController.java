package controllers;
import java.util.List;

import models.WallModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class WallController extends Controller {
	
	public static Result listAllWalls() {
		List<WallModel> walls = WallModel.findAll();
		return ok(Json.toJson(walls));
	}
	
	public static Result listWallsForLocation(Double startLat, Double startLong, Double stopLat, Double stopLong) {
		System.out.println("lat: " + startLat + " long: " + startLong);
		
		List<WallModel> walls = WallModel.findWallsOnArea(startLat, startLong, stopLat, stopLong);
		return ok(Json.toJson(walls));
	}
}
