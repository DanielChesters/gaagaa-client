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

    @FormUrlEncoded
    @POST("/oauth2/token")
    Authenticated authorizeUser(@Header("Authorization") String authorization,
                                @Field("grant_type") String grantType);

    @Headers({
            "Content-Type: application/json"
    })
    @GET("/1.1/lists/statuses.json")
    List<Tweet> getTwitterStream(@Query("slug") String slug,
                                 @Query("owner_screen_name") String screenName);

    @GET("/1.1/account/verify_credentials.json")
    TwitterUser verifyCredentials(@Header("Authorization") String authorization);

    @POST("/1.1/statuses/update.json")
    Tweet sendUpdate(@Header("Authorization") String authorization, @Body String status);

    @GET("/statuses/user_timeline.json")
    List<Tweet> getUserTimeline(@Query("screen_name") String screenName, @Query("count") int count);
}
