package com.nishanth.youtube;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;

import org.codehaus.jackson.map.ObjectMapper;

import com.nishanth.youtube.AccessTokenClass;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

@Path("/")
public class YoutubeDataAuthenticationController {

	private static final String GoogleAuth_URL = "https://accounts.google.com/o/oauth2/auth";
	private static final String Google_AccessToken_URL = "https://accounts.google.com/o/oauth2/token";
	private static final String Scope = "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fyoutube";
	private static final String Response_Type = "code";
	private static final String Access_Type = "offline";
	private static final String Page_Id = "none";

	@Context
	private ContextResolver<YoutubeAuthConfigPropertyFile> loadPropertyFile;

	YoutubeAuthConfigPropertyFile prop = LoadPropertyFiles.getPropertyFile();

	@GET
	@Path("/")
	public String intialPage()
	{
		return "Welcome to Google Auth API";
	}

	@GET
	@Path("/youtubeDataAPI")
	public String oAuth()
	{
		String url = GoogleAuth_URL+"?client_id="+prop.getClient_Id()+"&redirect_uri="+prop.getRedirect_uri()+"&scope="+Scope+"&response_type="+Response_Type+"&access_type="+Access_Type+"&pageId="+Page_Id;

		if(Desktop.isDesktopSupported()){
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(url));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}else{
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("xdg-open " + url);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		return "redirecting to /oauth2callback";
	}

	@Path("/oauth2callback")
	@GET
	public String oAuthCallback(@QueryParam("code") String code)
	{
		try {

			Client client = Client.create();
			WebResource webResource = client
					.resource(Google_AccessToken_URL);

			String input = "code="+code+"&"
					+ "client_id="+prop.getClient_Id()+"&"
					+ "client_secret="+prop.getClient_secret()+"&"
					+ "redirect_uri="+prop.getRedirect_uri_access_token()+"&"
					+ "grant_type=authorization_code";

			ClientResponse response = webResource.type("application/x-www-form-urlencoded")
					.post(ClientResponse.class, input);

			if (response.getStatus() != 200) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String str = response.getEntity(String.class);
			AccessTokenClass accessTokenClass = new ObjectMapper().readValue(str, AccessTokenClass.class);			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return "Access_Token Retrieved";
	}
}
