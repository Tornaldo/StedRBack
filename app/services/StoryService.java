package services;

import java.util.Collection;

import models.Place;
import models.Story;


/**
 * @author Simon Stastny
 *
 */
public interface StoryService {
	
	/**
	 * Queries Digitalt Fortalt with location and returns retrieved stories. Works with default radius.
	 * @param place
	 * @return collection of stories for this place
	 */
	public Collection<Story> getStoriesForPlace(Place place);
	
	
	/**
	 * Queries Digitalt Fortalt with location and returns retrieved stories. Works with default radius.
	 * @param place
	 * @param radius which stories should be retrieved from within
	 * @return collection of stories for this place
	 */
	public Collection<Story> getStoriesForPlace(Place place, Double radius);

}
