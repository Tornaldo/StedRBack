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

package utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Utility class implementing some convenience methods to work with JSON.
 * 
 * @author Simon Stastny
 *
 */
public class JsonUtils {
	
	/**
	 * Method for convenient lookup of a nested JsonElement in a tree structure.
	 * @param element element to search through
	 * @param path path to the element we search for (separated by '/' like in a directory structure)
	 * @return element on the specified path
	 */
	public static JsonElement findNestedElement(JsonElement element, String path) {
		// inspect the path
		List<String> wayDown = Arrays.asList(path.split("/")); 
		
		// go down the rabbit hole
		for (String item : wayDown) {
			element = element.getAsJsonObject().get(item);
		}
		
		return element;
	}

	public static Collection<String> findNestedStringCollection(JsonElement element, String path) {
		// inspect the path
		List<String> wayDown = Arrays.asList(path.split("/")); 
		
		Collection<String> found = Lists.newArrayList();
		
		// go down the rabbit hole
		for (String item : wayDown) {
			if(item.contains("*")) {
				JsonArray array = element.getAsJsonArray();
				
				for(int i = 0; i <array.size(); i++) {
					found.add(array.get(i).getAsString());
				}
			} else {
				element = element.getAsJsonObject().get(item);
			}
		}
		
		return found;
	}
	
}
