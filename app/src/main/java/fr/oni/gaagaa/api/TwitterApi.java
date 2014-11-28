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
    Observable<List<Tweet>> getTwitterStream(@Query(TwitterConstant.SLUG) String slug,
                                             @Query(TwitterConstant.OWNER_SCREEN_NAME) String screenName);

    @GET("/account/verify_credentials.json")
    Observable<TwitterUser> verifyCredentials();

    @POST("/statuses/update.json")
    Observable<Tweet> sendUpdate(@Body String status);

    @GET("/statuses/user_timeline.json")
    Observable<List<Tweet>> getUserTimeline(@Query(TwitterConstant.SCREEN_NAME) String screenName,
                                            @Query(TwitterConstant.COUNT) int count);

    @GET("/statuses/home_timeline.json")
    Observable<List<Tweet>> getHomeTimeline(@Query(TwitterConstant.COUNT) int count);

    @GET("/direct_messages.json")
    Observable<List<Tweet>> directMessages(@Query(TwitterConstant.COUNT) int count);

    @GET("/statuses/mentions_timeline.json")
    Observable<List<Tweet>> getMentionsTimeline(@Query(TwitterConstant.COUNT) int count);
}
