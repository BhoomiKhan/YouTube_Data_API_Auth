# YouTube_Data_API_Auth
Initial Commit, Working copy of OAuth Implementation of Youtube Data API

Important 

src/main/resources has youtubeDataAuthConfig.properties which has

youtubeDataAuthAPI.CLIENT_ID=

youtubeDataAuthAPI.CLIENT_SECRET=

youtubeDataAuthAPI.REDIRECT_URI=https%3A%2F%2Flocalhost%3A8443%2Foauth2callback

youtubeDataAuthAPI.REDIRECT_URI_FOR_ACCESS_TOKEN=https://localhost:8443/oauth2callback

Note: I have put the example of REDIRECT_URI and REDIRECT_URI_FOR_ACCESS_TOKEN above, you can follow that and don't forget to add Redirect URI's in the app console. (This is Important).

Need to fill in these values in there so that the values will be used in the application for authentication to use youtube data api. 

Note: 
- Without these values the application would not run.
- You would get CLIENT_ID, CLIENT_SECRET values in here "https://console.developers.google.com/project" by creating a new app, and then in API's & Auth in Credentials section you can create a new Client_Id.
- After that, in credentials section you can add a REDIRECT_URI.

How to run, 
After you check out the project, 
you can create a new maven build project and type in "clean install jetty:run" as goals in eclipse.
