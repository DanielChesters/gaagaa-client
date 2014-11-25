package fr.oni.gaagaa.activity;

import android.net.Uri;
import android.os.AsyncTask;
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

public class TwitterAuthActivity extends ActionBarActivity {
    private static final String TAG = TwitterAuthActivity.class.getSimpleName();
    private static final String CALLBACK = "oauth://twitter";

    private WebView webView;
    private String authUrl;

    private OAuthProvider provider;
    private OAuthConsumer consumer;

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

    private void onOAuthCallback(final Uri uri) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                String pinCode = uri.getQueryParameter("oauth_verifier");
                try {
                    provider.retrieveAccessToken(consumer, pinCode);
                    String token = consumer.getToken();
                    String tokenSecret = consumer.getTokenSecret();
                    PrefUtil.setTwitterToken(TwitterAuthActivity.this, token);
                    PrefUtil.setTwitterTokenSecret(TwitterAuthActivity.this, tokenSecret);
                } catch (OAuthMessageSignerException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (OAuthNotAuthorizedException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (OAuthExpectationFailedException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (OAuthCommunicationException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                finish();
            }
        }
                .execute();
    }

    private void initTwitter() {
        consumer = new DefaultOAuthConsumer(Config.TWITTER_API_KEY, Config.TWITTER_API_SECRET);

        provider = new DefaultOAuthProvider(
                "https://api.twitter.com/oauth/request_token",
                "https://api.twitter.com/oauth/access_token",
                "https://api.twitter.com/oauth/authorize"
        );

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    authUrl = provider.retrieveRequestToken(consumer, CALLBACK);
                    Log.d(TAG, "authUrl " + authUrl);
                } catch (OAuthMessageSignerException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (OAuthNotAuthorizedException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (OAuthExpectationFailedException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (OAuthCommunicationException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(authUrl)) {
                    webView.loadUrl(authUrl);
                }
            }
        }
                .execute();

    }
}
