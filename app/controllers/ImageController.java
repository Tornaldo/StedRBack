package controllers;

import java.util.Collection;

import com.google.common.collect.Lists;

import models.Image;
import models.Place;
import models.Story;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.DigitaltFortaltRetriever;
import retrievers.FlickrRetriever;
import retrievers.InstagramRetriever;
import retrievers.flickr.AdditionalDataQuery;
import services.ImageService;
import services.PlaceService;
import services.StoryService;

public class ImageController extends Controller {

	public static Result listImagesForTag(String tag) {
		ImageService imageService = new InstagramRetriever();
		
		Collection<Image> images = imageService.getImagesForTag(tag);
		
		return ok(Json.toJson(images));
	}
}
