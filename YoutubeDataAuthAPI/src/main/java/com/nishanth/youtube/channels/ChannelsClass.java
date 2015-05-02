package com.nishanth.youtube.channels;

import java.util.List;

public class ChannelsClass {

	private String kind;
	private String etag;
	private String nextPageToken;
	private String prevPageToken;
	private pageInfoClass pageInfo;
	private List<ItemsClass> items;
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
	public String getNextPageToken() {
		return nextPageToken;
	}
	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}
	public String getPrevPageToken() {
		return prevPageToken;
	}
	public void setPrevPageToken(String prevPageToken) {
		this.prevPageToken = prevPageToken;
	}
	public pageInfoClass getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(pageInfoClass pageInfo) {
		this.pageInfo = pageInfo;
	}
	public List<ItemsClass> getItems() {
		return items;
	}
	public void setItems(List<ItemsClass> items) {
		this.items = items;
	}
	
	
}
