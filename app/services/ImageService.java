package services;

import java.util.Collection;

import models.Image;

/**
 * 
 * @author Simon Stastny
 *
 */
public interface ImageService {
	
	public Collection<Image> getImagesForTag(String tag);

}
