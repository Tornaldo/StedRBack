package services;

import java.util.Collection;

import models.Place;
import models.Story;

public interface StoryService {
	
	public Collection<Story> getStoriesForPlace(Place place);
	
	public Collection<Story> getStoriesForPlace(Place place, Double radius);

}
