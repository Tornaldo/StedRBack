package services;

import java.util.Collection;

import models.Image;

public interface ImageService {
	
	public Collection<Image> getImagesForTag(String tag);

}
