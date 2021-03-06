/*
 *	Copyright (c) 2013, Odd Fredrik Rogstad, Christian Frøystad, Simon Stastny, Knut Nergård
 *	All rights reserved.
 *	
 *	Redistribution and use in source and binary forms, with or without
 *	modification, are permitted provided that the following conditions are met:
 *	* Redistributions of source code must retain the above copyright
 *	  notice, this list of conditions and the following disclaimer.
 *	* Redistributions in binary form must reproduce the above copyright
 *	  notice, this list of conditions and the following disclaimer in the
 *	  documentation and/or other materials provided with the distribution.
 *	* Neither the name of the project nor the 
 *	  names of its contributors may be used to endorse or promote products
 *	  derived from this software without specific prior written permission.
 *	
 *	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNERS BE LIABLE FOR ANY
 *	DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
			image.url = JsonUtils.findNestedElementAsString(dataElement, "images/standard_resolution/url");
			image.fullName = JsonUtils.findNestedElementAsString(dataElement, "user/full_name");
			image.commentCount = JsonUtils.findNestedElementAsInteger(dataElement, "comments/count");
			image.likesCount = JsonUtils.findNestedElementAsInteger(dataElement, "likes/count");
			image.caption = JsonUtils.findNestedElementAsString(dataElement, "caption/text");
			image.tags = JsonUtils.findNestedStringCollection(dataElement, "tags/*");

			images.add(image);
		}

		return images;
	}
}
