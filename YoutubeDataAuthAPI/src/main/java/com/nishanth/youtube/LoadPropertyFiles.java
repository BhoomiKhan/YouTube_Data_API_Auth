package com.nishanth.youtube;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

@Provider
public class LoadPropertyFiles implements ContextResolver<YoutubeAuthConfigPropertyFile> {
    
	private static YoutubeAuthConfigPropertyFile properties = null;
    
    public LoadPropertyFiles() {
        super();
        properties = new YoutubeAuthConfigPropertyFile();

        Properties prop = new Properties();
        String propFileName = "youtubeDataAuthConfig.properties";

		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {

				prop.load(inputStream);
				properties.setClient_Id(prop.get("youtubeDataAuthAPI.CLIENT_ID").toString());
				properties.setClient_secret(prop.get("youtubeDataAuthAPI.CLIENT_SECRET").toString());
				properties.setRedirect_uri(prop.get("youtubeDataAuthAPI.REDIRECT_URI").toString());
				properties.setRedirect_uri_access_token(prop.get("youtubeDataAuthAPI.REDIRECT_URI_FOR_ACCESS_TOKEN").toString());
				
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static YoutubeAuthConfigPropertyFile getPropertyFile() {
        return properties;
    }

	public YoutubeAuthConfigPropertyFile getContext(Class<?> arg0) {
		return null;
	}
}