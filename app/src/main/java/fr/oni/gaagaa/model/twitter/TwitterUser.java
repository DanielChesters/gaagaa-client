package fr.oni.gaagaa.model.twitter;

import com.google.gson.annotations.SerializedName;

import fr.oni.gaagaa.api.TwitterConstant;

public class TwitterUser {
    @SerializedName("name")
    private String name;
    @SerializedName("profile_image_url_https")
    private String profileImageUrlHttps;
    @SerializedName(TwitterConstant.SCREEN_NAME)
    private String screenName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrlHttps() {
        return profileImageUrlHttps;
    }

    public void setProfileImageUrlHttps(String profileImageUrlHttps) {
        this.profileImageUrlHttps = profileImageUrlHttps;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
