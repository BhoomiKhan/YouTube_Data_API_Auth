package com.nishanth.youtube;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nishanth.youtube.AccessTokenClass;
import com.nishanth.youtube.channels.ChannelsClass;
import com.nishanth.youtube.channels.ContentDetailsClass;
import com.nishanth.youtube.channels.ItemsClass;
import com.nishanth.youtube.channels.RelatedPlaylists;
import com.nishanth.youtube.channels.SnippetClass;
import com.nishanth.youtube.uiobjects.favouriteVideoObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class YoutubeDataAuthenticationController {

	private static final String GoogleAuth_URL = "https://accounts.google.com/o/oauth2/auth";
	private static final String Google_AccessToken_URL = "https://accounts.google.com/o/oauth2/token";
	private static final String Youtube_Scope = "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fyoutube";
	private static final String Response_Type = "code";
	private static final String Access_Type = "offline";

	private AccessTokenClass accessTokenClass;
	private ChannelsClass channels;
	
	@Value("${youtube.youtubeDataAuthAPI.CLIENT_ID}")
	private String CLIENT_ID;

	@Value("${youtube.youtubeDataAuthAPI.CLIENT_SECRET}")
	private String CLIENT_SECRET;

	@Value("${youtube.redirect.REDIRECT_URI}")
	private String REDIRECT_URI;

	@Value("${youtube.redirect.REDIRECT_URI_FOR_ACCESS_TOKEN}")
	private String REDIRECT_URI_FOR_ACCESS_TOKEN;
	
	@Value("${youtube.refresh.REFRESH_TOKEN}")
	private String REFRESH_TOKEN;
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody String initialPage(HttpServletRequest httpReq) 
	{
		return "Welcome to YouTube Data API";
	}

	@RequestMapping( value="/youtubeAuth", method = RequestMethod.GET)
	public @ResponseBody String youtubeAuth()
	{
		String url = GoogleAuth_URL+"?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&scope="+Youtube_Scope+"&response_type="+Response_Type+"&access_type="+Access_Type;

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

	@RequestMapping( value="/oauth2callback", method = RequestMethod.GET)
	public @ResponseBody String oAuthCallback(@QueryParam("code") String code)
	{
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(Google_AccessToken_URL);

			String input = "code="+code+"&"
					+ "client_id="+CLIENT_ID+"&"
					+ "client_secret="+CLIENT_SECRET+"&"
					+ "redirect_uri="+REDIRECT_URI_FOR_ACCESS_TOKEN+"&"
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
			System.out.println("ACCESS_TOKEN = "+accessTokenClass.getAccess_token());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return "Access_Token Retrieved";
	}
	
	
	@RequestMapping( value="/channels", method = RequestMethod.GET)
	public @ResponseBody String channels()
	{
		String channelUrl = "https://www.googleapis.com/youtube/v3/channels?part=contentDetails&mine=true";
		ClientResponse response = null;
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(channelUrl);
			if(accessTokenClass == null)
			{
				refreshAccessToken();
			}
			response = webResource.header("Authorization","Bearer "+accessTokenClass.getAccess_token()).get(ClientResponse.class);
			if (response.getStatus() != 200 && response.getStatus() != 401 ) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			if (response.getStatus() == 401) {
				response = null;
				System.out.println("401 Unauthorized");
				refreshAccessToken();
				response = webResource.header("Authorization","Bearer "+accessTokenClass.getAccess_token()).get(ClientResponse.class);
			}
			String str = response.getEntity(String.class);
			channels = new ObjectMapper().readValue(str, ChannelsClass.class);
			for(ItemsClass item : channels.getItems())
			{
				ContentDetailsClass content = item.getContentDetails();
				RelatedPlaylists relatedPlaylists = content.getRelatedPlaylists();
				System.out.println("Likes = "+ relatedPlaylists.getLikes());
				System.out.println("Favourites = "+ relatedPlaylists.getFavorites());
				System.out.println("Uploads = "+ relatedPlaylists.getUploads());
				System.out.println("WatchHistory = "+ relatedPlaylists.getWatchHistory());
				System.out.println("WatchLater = "+ relatedPlaylists.getWatchLater());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return "Channels_Retreived";
	}
	
	@RequestMapping(value ="/favourites", method = RequestMethod.GET)
	public @ResponseBody String favourites(HttpServletRequest httpReq)
	{
		List<favouriteVideoObject> favouriteVideosList = new ArrayList<favouriteVideoObject>();
		ChannelsClass favouriteChannel = new ChannelsClass();
		String favouritesUrl = "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails,snippet&playlistId=FLHipOXS1Mf4VoAcIqJFS3qA";
		ClientResponse response = null;
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(favouritesUrl);
			if(accessTokenClass == null)
			{
				refreshAccessToken();
			}
			response = webResource.header("Authorization","Bearer "+accessTokenClass.getAccess_token()).get(ClientResponse.class);
			if (response.getStatus() != 200 && response.getStatus() != 401) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			
			if (response.getStatus() == 401) {
				response = null;
				System.out.println("401 Unauthorized");
				refreshAccessToken();
				response = webResource.header("Authorization","Bearer "+accessTokenClass.getAccess_token()).get(ClientResponse.class);
			}

			String str = response.getEntity(String.class);
			favouriteChannel = new ObjectMapper().readValue(str, ChannelsClass.class);
			
			for(ItemsClass item :favouriteChannel.getItems())
			{
				ContentDetailsClass content  = item.getContentDetails();
				SnippetClass snippet = item.getSnippet();
				favouriteVideoObject f = new favouriteVideoObject();
				f.setVideoId(content.getVideoId());
				f.setTitle(snippet.getTitle());
				if(snippet.getThumbnails() != null)
					f.setImageUrl(snippet.getThumbnails().getDefaults().getUrl());
				favouriteVideosList.add(f);
			}
			if(favouriteChannel.getNextPageToken() !=  null)
			{
				favouritesUrl = favouritesUrl+"&pageToken="+favouriteChannel.getNextPageToken();
				recursiveCall(favouritesUrl,favouriteChannel,favouriteVideosList,favouriteChannel.getNextPageToken().length());
				for(favouriteVideoObject f : favouriteVideosList)
				{
					System.out.print("VideoId = "+f.getVideoId() +" ");
					System.out.print("Title = "+f.getTitle() +" ");
					if(f.getImageUrl() != null)
						System.out.print("ImageUrl = "+f.getImageUrl() +" ");
					System.out.println();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
 		return "Favourites_Retreived";
	}

	private List<favouriteVideoObject> recursiveCall(String favouritesUrl,ChannelsClass favouriteChannel, List<favouriteVideoObject> favouriteVideosList, int lenOfNextPageToken) throws JsonParseException, JsonMappingException, IOException
	{
		Client client = Client.create();
		WebResource webResource = client.resource(favouritesUrl);
		ClientResponse response = webResource.header("Authorization","Bearer "+accessTokenClass.getAccess_token()).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			System.out.println("Response = "+response.getStatus());
			System.out.println("Content  = "+response.getEntity(String.class));
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		String str = response.getEntity(String.class);
		favouriteChannel = null;
		favouriteChannel = new ObjectMapper().readValue(str, ChannelsClass.class);
		for(ItemsClass item :favouriteChannel.getItems())
		{
			ContentDetailsClass content  = item.getContentDetails();
			SnippetClass snippet = item.getSnippet();
			favouriteVideoObject f = new favouriteVideoObject();
			f.setVideoId(content.getVideoId());
			f.setTitle(snippet.getTitle());
			f.setImageUrl(snippet.getThumbnails().getDefaults().getUrl());
			favouriteVideosList.add(f);
		}
		if(favouriteChannel.getNextPageToken() !=  null)
		{
			favouritesUrl = favouritesUrl.substring(0, favouritesUrl.length()-lenOfNextPageToken-11);
			favouritesUrl = favouritesUrl+"&pageToken="+favouriteChannel.getNextPageToken();
			recursiveCall(favouritesUrl,favouriteChannel,favouriteVideosList,favouriteChannel.getNextPageToken().length());
		}
		return favouriteVideosList;
	}
	
	private void refreshAccessToken()
	{
		String input = "client_id="+CLIENT_ID+"&"
				+ "client_secret="+CLIENT_SECRET+"&"
				+ "refresh_token="+REFRESH_TOKEN+"&"
				+ "grant_type=refresh_token";
		try{
			Client client = Client.create();
			WebResource webResource1 = client.resource(Google_AccessToken_URL);
			ClientResponse response = webResource1.type("application/x-www-form-urlencoded")
					.post(ClientResponse.class, input);
			
			if (response.getStatus() != 200) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			String str = response.getEntity(String.class);
			accessTokenClass = null;
			accessTokenClass = new ObjectMapper().readValue(str, AccessTokenClass.class);
			System.out.println("Access_Token Refreshed");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
