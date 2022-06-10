package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public String imageUrl;
    public User user;
    public String id;
    public boolean retweeted;
    public boolean liked;
    public String screenName;

    //public String

    // empty constructor needed by the Parceler library
    public Tweet() {

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if (jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        if (jsonObject.getJSONObject("entities").has("media")) {
            tweet.imageUrl = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
        }
        tweet.id = jsonObject.getString("id_str");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.liked = jsonObject.getBoolean("favorited");
        tweet.screenName = jsonObject.getJSONObject("user").getString("screen_name");
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
