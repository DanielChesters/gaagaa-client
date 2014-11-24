package fr.oni.gaagaa.api;

import java.util.List;

import fr.oni.gaagaa.model.twitter.Authenticated;
import fr.oni.gaagaa.model.twitter.Tweet;
import fr.oni.gaagaa.model.twitter.TwitterUser;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

public interface TwitterApi {

    @GET("/lists/statuses.json")
    List<Tweet> getTwitterStream(@Query("slug") String slug,
                                 @Query("owner_screen_name") String screenName);

    @GET("/account/verify_credentials.json")
    TwitterUser verifyCredentials(@Header("Authorization") String authorization);

    @POST("/statuses/update.json")
    Tweet sendUpdate(@Header("Authorization") String authorization, @Body String status);

    @GET("/statuses/user_timeline.json")
    List<Tweet> getUserTimeline(@Query("screen_name") String screenName, @Query("count") int count);
}
