package controllers;

import java.util.Collection;

import models.Image;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.InstagramRetriever;
import services.ImageService;

/**
 * 
 * @author Simon Stastny
 *
 */
public class ImageController extends Controller {

	/**
	 * Find all photos associated with this tag in the ImageService used.
	 * @param tag tag to find photos for
	 * @return JSON file with images
	 */
	public static Result listImagesForTag(String tag) {
		ImageService imageService = new InstagramRetriever(); // using instagram as the image service
		
		Collection<Image> images = imageService.getImagesForTag(tag);
		
		return ok(Json.toJson(images));
	}
}
