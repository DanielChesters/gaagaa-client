package fr.oni.gaagaa.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import fr.oni.gaagaa.Config;
import fr.oni.gaagaa.R;
import fr.oni.gaagaa.util.PrefUtil;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TwitterAuthActivity extends ActionBarActivity {
    private static final String TAG = TwitterAuthActivity.class.getSimpleName();
    private static final String CALLBACK = "oauth://twitter";

    private WebView webView;
    private String authUrl;

    private OAuthProvider provider;
    private OAuthConsumer consumer;
    private Subscription twitterWebAuthSubscription;
    private Subscription twitterTokenSubscription;

    @Override
    protected void onDestroy() {
        twitterWebAuthSubscription.unsubscribe();
        twitterTokenSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_twitter_auth);

        initTwitter();

        webView = (WebView) findViewById(R.id.twitter_auth_webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("oauth")) {
                    Uri uri = Uri.parse(url);
                    onOAuthCallback(uri);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        if (TextUtils.isEmpty(authUrl)) {
            webView.loadUrl(authUrl);
        }
    }

    private void onOAuthCallback(Uri uri) {
        Action1<Token> onNextAction = new Action1<Token>() {
            @Override
            public void call(Token token) {
                PrefUtil.setTwitterToken(TwitterAuthActivity.this, token.getKey());
                PrefUtil.setTwitterTokenSecret(TwitterAuthActivity.this, token.getSecret());
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, throwable.getMessage(), throwable);
            }
        };

        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {
                finish();
            }
        };

        twitterTokenSubscription = getTwitterToken(uri).subscribe(onNextAction, onErrorAction,
                onCompleteAction);
    }

    private void initTwitter() {
        consumer = new DefaultOAuthConsumer(Config.TWITTER_API_KEY, Config.TWITTER_API_SECRET);

        provider = new DefaultOAuthProvider(
                "https://api.twitter.com/oauth/request_token",
                "https://api.twitter.com/oauth/access_token",
                "https://api.twitter.com/oauth/authorize"
        );

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                authUrl = s;
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, throwable.getMessage(), throwable);
            }
        };

        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {
                if (!TextUtils.isEmpty(authUrl)) {
                    webView.loadUrl(authUrl);
                }
            }
        };

        twitterWebAuthSubscription = getTwitterWebAuth()
                .subscribe(onNextAction, onErrorAction, onCompleteAction);
    }

    private Observable<String> getTwitterWebAuth() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String authUrl = provider.retrieveRequestToken(consumer, CALLBACK);
                    subscriber.onNext(authUrl);
                    subscriber.onCompleted();
                } catch (OAuthMessageSignerException | OAuthNotAuthorizedException
                        | OAuthExpectationFailedException | OAuthCommunicationException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private Observable<Token> getTwitterToken(final Uri uri) {
        return Observable.create(new Observable.OnSubscribe<Token>() {

            @Override
            public void call(Subscriber<? super Token> subscriber) {
                String pinCode = uri.getQueryParameter("oauth_verifier");
                try {
                    provider.retrieveAccessToken(consumer, pinCode);
                    String token = consumer.getToken();
                    String tokenSecret = consumer.getTokenSecret();
                    subscriber.onNext(new Token(token, tokenSecret));
                    subscriber.onCompleted();
                } catch (OAuthMessageSignerException | OAuthNotAuthorizedException
                        | OAuthExpectationFailedException | OAuthCommunicationException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private static final class Token {
        private String key;
        private String secret;

        private Token(String key, String secret) {
            this.key = key;
            this.secret = secret;
        }

        public String getKey() {
            return key;
        }

        public String getSecret() {
            return secret;
        }
    }
}
