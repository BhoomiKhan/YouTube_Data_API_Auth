package com.nishanth.youtube.channels;

public class ItemsClass {

	private String kind;
	private String etag;
	private String id;
	private SnippetClass snippet;
	private ContentDetailsClass contentDetails;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getEtag() {
		return etag;
	}
	public void setEtag(String etag) {
		this.etag = etag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SnippetClass getSnippet() {
		return snippet;
	}
	public void setSnippet(SnippetClass snippet) {
		this.snippet = snippet;
	}
	public ContentDetailsClass getContentDetails() {
		return contentDetails;
	}
	public void setContentDetails(ContentDetailsClass contentDetails) {
		this.contentDetails = contentDetails;
	}
	
	
}
