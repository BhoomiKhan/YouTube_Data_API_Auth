package com.nishanth.youtube;

public class YoutubeAuthConfigPropertyFile {

	private String client_Id;
	private String client_secret;
	private String redirect_uri;
	private String redirect_uri_access_token;
	public String getClient_Id() {
		return client_Id;
	}
	public void setClient_Id(String client_Id) {
		this.client_Id = client_Id;
	}
	public String getClient_secret() {
		return client_secret;
	}
	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}
	public String getRedirect_uri() {
		return redirect_uri;
	}
	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}
	public String getRedirect_uri_access_token() {
		return redirect_uri_access_token;
	}
	public void setRedirect_uri_access_token(String redirect_uri_access_token) {
		this.redirect_uri_access_token = redirect_uri_access_token;
	}

}
