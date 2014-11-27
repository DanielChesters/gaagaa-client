package fr.oni.gaagaa.api;

import java.util.List;

import fr.oni.gaagaa.model.twitter.Tweet;
import fr.oni.gaagaa.model.twitter.TwitterUser;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

public interface TwitterApi {

    @GET("/lists/statuses.json")
    Observable<List<Tweet>> getTwitterStream(@Query("slug") String slug,
                                 @Query("owner_screen_name") String screenName);

    @GET("/account/verify_credentials.json")
    Observable<TwitterUser> verifyCredentials();

    @POST("/statuses/update.json")
    Observable<Tweet> sendUpdate(@Body String status);

    @GET("/statuses/user_timeline.json")
    Observable<List<Tweet>> getUserTimeline(@Query("screen_name") String screenName, @Query("count") int count);

    @GET("/statuses/home_timeline.json")
    Observable<List<Tweet>> getHomeTimeline(@Query("count") int count);
}
