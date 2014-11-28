package fr.oni.gaagaa.model.twitter;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class Tweet {
    @SerializedName("created_at")
    private DateTime dateCreated;
    @SerializedName("id")
    private String id;
    @SerializedName("text")
    private String text;
    @SerializedName("in_reply_to_status_id")
    private String inReplyToStatusId;
    @SerializedName("in_reply_to_user_id")
    private String inReplyToUserId;
    @SerializedName("in_reply_to_screen_name")
    private String inReplyToScreenName;
    @SerializedName("user")
    private TwitterUser user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(String inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public String getInReplyToUserId() {
        return inReplyToUserId;
    }

    public void setInReplyToUserId(String inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public TwitterUser getUser() {
        return user;
    }

    public void setUser(TwitterUser user) {
        this.user = user;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tweet tweet = (Tweet) o;

        if (id != null ? !id.equals(tweet.id) : tweet.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
