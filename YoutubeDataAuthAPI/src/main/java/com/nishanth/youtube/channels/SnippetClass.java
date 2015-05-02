package com.nishanth.youtube.channels;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SnippetClass {

	private String title;
	private ThumbnailsClass thumbnails;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ThumbnailsClass getThumbnails() {
		return thumbnails;
	}
	public void setThumbnails(ThumbnailsClass thumbnails) {
		this.thumbnails = thumbnails;
	}
	
	
}
