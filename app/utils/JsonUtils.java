package utils;

import java.util.Arrays;
import java.util.List;

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

}
