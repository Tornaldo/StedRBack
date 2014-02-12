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
	 * @param placeId Id of the place to look up the stories for.
	 * @return JSON file with stories.
	 */
	public static Result listStoriesForPlace(String placeId) {
		return listStoriesForPlaceInRadius(placeId, null);
	}

	/**
	 * Finds all stories associated with this place according to the story server. Works with explicit radius.
	 * 
	 * @param placeId Id of the place to look up the stories for.
	 * @param radius Radius from which stories should be retrieved within.
	 * @return JSON file with stories.
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
