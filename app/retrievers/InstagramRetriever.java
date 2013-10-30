package retrievers;

import java.util.Collection;

import models.Image;
import services.ImageService;
import services.StedrConstants;
import utils.HttpUtils;
import utils.JsonUtils;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Implementation of ImageService using Instagram.
 * 
 * @author Simon Stastny
 * 
 */
public class InstagramRetriever implements ImageService {

	// base url for the instagram api
	private static final String API_URL = "https://api.instagram.com/v1/";

	// method name for retrieving photos for tags
	private static final String TAG_METHOD_NAME = "tags/%s/media/recent";

	@Override
	public Collection<Image> getImagesForTag(String tag) {
		Collection<Image> images = Lists.newArrayList();

		// build url for the api call
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(API_URL);
		urlBuilder.append(String.format(TAG_METHOD_NAME, tag));
		urlBuilder.append("?access_token=").append(StedrConstants.INSTAGRAM_ACCESS_TOKEN);

		// get response from the api
		String document = HttpUtils.getDocument(urlBuilder.toString());

		// parse the response into JSON document
		JsonParser jp = new JsonParser();
		JsonElement jsonDocument = jp.parse(document);

		// find data elements
		JsonArray dataArray = JsonUtils.findNestedElement(jsonDocument, "data").getAsJsonArray();

		// iterate over and find info about images
		for (JsonElement dataElement : dataArray) {
			Image image = new Image();
			image.url = JsonUtils.findNestedElement(dataElement, "images/standard_resolution/url").getAsString();
			image.fullName = JsonUtils.findNestedElement(dataElement, "user/full_name").getAsString();

			images.add(image);
		}

		return images;
	}
}
