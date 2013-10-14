package models;

import java.util.List;

public class Story {
	
	String title;
	
	List<String> pictures;
	
	List<String> creators;

	List<String> tags;

	public String getTitle() {
		return title;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public List<String> getCreators() {
		return creators;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	public void setCreators(List<String> creators) {
		this.creators = creators;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
