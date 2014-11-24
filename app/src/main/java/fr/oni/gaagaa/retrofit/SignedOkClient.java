package fr.oni.gaagaa.retrofit;

import java.io.IOException;
import java.net.HttpURLConnection;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import retrofit.client.OkClient;
import retrofit.client.Request;

public class SignedOkClient extends OkClient {
    private OAuthConsumer consumer;

    public SignedOkClient(OAuthConsumer consumer) {
        super();
        this.consumer = consumer;
    }

    @Override
    protected HttpURLConnection openConnection(Request request) throws IOException {
        HttpURLConnection connection = super.openConnection(request);
        try {
            consumer.sign(connection);
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
