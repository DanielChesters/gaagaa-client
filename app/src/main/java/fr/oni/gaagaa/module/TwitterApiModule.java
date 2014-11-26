package fr.oni.gaagaa.module;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import fr.oni.gaagaa.Config;
import fr.oni.gaagaa.api.TwitterApi;
import fr.oni.gaagaa.model.twitter.Tweet;
import fr.oni.gaagaa.retrofit.SignedOkClient;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TwitterApiModule {
    private TwitterApi twitterApi;

    public void init(String token, String tokenSecret) {
        OAuthConsumer consumer = new DefaultOAuthConsumer(Config.TWITTER_API_KEY, Config.TWITTER_API_SECRET);
        consumer.setTokenWithSecret(token, tokenSecret);
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://api.twitter.com/1.1/")
                .setClient(new SignedOkClient(consumer))
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        twitterApi = adapter.create(TwitterApi.class);
    }

    public Observable<List<Tweet>> getTwitterStream(String slug, String screenName) {
        return twitterApi.getTwitterStream(slug, screenName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Tweet>> getUserTimeline(String screenName, int count) {
        return twitterApi.getUserTimeline(screenName, count)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
