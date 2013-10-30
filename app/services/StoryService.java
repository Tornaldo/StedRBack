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
	 * Queries Story Service with location of the place and returns retrieved stories. Works with default radius.
	 * @param place Place to look up the stories for.
	 * @return Collection of stories.
	 */
	public Collection<Story> getStoriesForPlace(Place place);
	
	
	/**
	 * Queries Story Service with location of the place and returns retrieved stories. Works with specified radius.
	 * @param place Place to look up the stories for.
	 * @param radius Radius from which stories should be retrieved within.
	 * @return Collection of stories.
	 */
	public Collection<Story> getStoriesForPlace(Place place, Double radius);

}
