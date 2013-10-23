package retrievers;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.Image;
import services.ImageService;
import services.StedrConstants;

public class InstagramRetriever implements ImageService {

	private static final String API_URL = "https://api.instagram.com/v1/";

	private static final String TAG_METHOD_NAME = "tags/%s/media/recent";

	@Override
	public Collection<Image> getImagesForTag(String tag) {
		try {

			Collection<Image> images = Lists.newArrayList();
			
			StringBuilder urlBuilder = new StringBuilder();

			urlBuilder.append(API_URL);
			urlBuilder.append(String.format(TAG_METHOD_NAME, tag));
			urlBuilder.append("?access_token=").append(StedrConstants.INSTAGRAM_ACCESS_TOKEN);

			InputStream is = retrieveStream(urlBuilder.toString());

			StringWriter writer = new StringWriter();
			IOUtils.copy(is, writer);
			String jsonData = writer.toString();

			JsonParser jp = new JsonParser();		
			JsonElement element = jp.parse(jsonData);
			
			JsonObject asJsonObject = element.getAsJsonObject();
			
			JsonArray asJsonArray = asJsonObject.get("data").getAsJsonArray();
			
			for (JsonElement jsonElement : asJsonArray) {
				Image image = new Image();
				image.url = jsonElement.getAsJsonObject().get("images").getAsJsonObject().get("standard_resolution").getAsJsonObject().get("url").getAsString();
				image.fullName = jsonElement.getAsJsonObject().get("user").getAsJsonObject().get("full_name").getAsString();
				images.add(image);
			}
			
			return images;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	private InputStream retrieveStream(String url) {

		DefaultHttpClient client = new DefaultHttpClient();

		HttpGet getRequest = new HttpGet(url);

		try {

			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} catch (IOException e) {
			getRequest.abort();
		}

		return null;
	}

}
