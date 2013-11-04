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

import com.google.common.collect.Lists;

import models.Place;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import retrievers.FlickrRetriever;
import services.PlaceService;

/**
 * 
 * @author Simon Stastny
 *
 */
public class PlaceController extends Controller {

	/**
	 * Returns all the places in the PlaceService used.
	 * 
	 * @return JSON file with places
	 */
	public static Result listAllPlaces() {
		PlaceService placeService = new FlickrRetriever(); // using flickr as the place service
		Collection<Place> allPlaces = placeService.getAllPlaces();

		if(allPlaces == null) {
			allPlaces = Lists.newArrayList();
		}
		
		return ok(Json.toJson(allPlaces));
	}

	/**
	 * Finds all the places in a rectangle area defined by bottom-left and top-right corner on a map.
	 * 
	 * @param latBL minimal latitude
	 * @param lngBL minimal longitude
	 * @param latTR maximal latitude
	 * @param lngTR maximal longitude
	 * @return JSON file with places
	 */
	public static Result listPlacesInArea(Double latBL, Double lngBL, Double latTR, Double lngTR) {
		PlaceService placeService = new FlickrRetriever(); // using flickr as the place service
		Collection<Place> places = placeService.getPlacesInArea(latBL, lngBL, latTR, lngTR);

		return ok(Json.toJson(places));
	}
}
