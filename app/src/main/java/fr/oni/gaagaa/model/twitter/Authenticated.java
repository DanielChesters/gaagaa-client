package fr.oni.gaagaa.model.twitter;

import com.google.gson.annotations.SerializedName;

public class Authenticated {

    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
