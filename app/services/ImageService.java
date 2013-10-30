package services;

import java.util.Collection;

import models.Image;

/**
 * 
 * @author Simon Stastny
 *
 */
public interface ImageService {
	/**
	 *  Find all photos associated with this tag.
	 * @param tag Tag to find photos for.
	 * @return Collection of photos.
	 */
	public Collection<Image> getImagesForTag(String tag);

}
