package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class fix extends AppCompatActivity {

    public static final String TAG = "ReplyActivity";
    public static final int MAX_TWEET_LENGTH = 140;

    EditText retweetInput;
    Button btnSubmitReply;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        retweetInput = findViewById(R.id.retweetInput);
        btnSubmitReply = findViewById(R.id.btnSubmitReply);

        // Set click listener on button
        /*
        btnSubmitReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = retweetInput.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ReplyActivity.this, "Reply cannot be empty.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ReplyActivity.this, "Reply cannot exceed 140 characters.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Make an API call to Twitter to publish the reply
                client.publishReply(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to reply to tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published reply says: " + tweet.body);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent); // set result code and bundle data for response
                            finish(); // close current activity and returns/passes data to parent
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish reply", throwable);
                    }
                });
            }
        });
    */

    }
}