package fr.oni.gaagaa.model.twitter;

import com.google.gson.annotations.SerializedName;

public class TwitterUser {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
