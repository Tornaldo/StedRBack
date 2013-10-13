package models;

import java.util.List;

public class Story {
	
	//FIXME which fields does a story have?
	
	String title;
	
	List<String> pictures;

	public String getTitle() {
		return title;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}
	
	

}
