package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    TwitterClient client;

    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
        this.client = TwitterApp.getRestClient(context);
    }

    @NonNull
    @Override
    // For each row, inflate a layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view =  LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
       return new ViewHolder(view);
    }

    @Override
    // Bind values based on the position of the element
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data
        Tweet tweet = tweets.get(position);
        // Bind the data with view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
       tweets.addAll(list);
       notifyDataSetChanged();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivImage;
        TextView tvTimestamp;
        TextView tvName;
        ImageButton btnRetweet;
        ImageButton btnLike;
        Button btnReply;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById((R.id.ivProfileImage));
            tvBody = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnReply = itemView.findViewById(R.id.btnReply);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTPro-Roman.otf");
            tvBody.setTypeface(font);
            tvScreenName.setTypeface(font);
            tvTimestamp.setTypeface(font);
            tvName.setTypeface(font, Typeface.BOLD);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@"+tweet.user.screenName + " ·");
            tvTimestamp.setText(getRelativeTimeAgo(tweet.createdAt));
            tvName.setText(tweet.user.name);
            int radius = 70;
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            Glide.with(context).load(tweet.imageUrl).centerCrop().
                    transform(new RoundedCorners(radius)).into(ivImage);

            if (tweet.liked) {
                btnLike.setImageResource(R.drawable.ic_vector_heart);
            }
            if (!tweet.liked) {
                btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
            }
            if (tweet.retweeted) {
                btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
            }
            if (!tweet.retweeted) {
                btnRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
            }


            // Reply
            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ReplyActivity.class);
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(intent);
                }
            });


            // Retweet/Unretweet Button
            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!tweet.retweeted) {
                        client.publishRetweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                tweet.retweeted = true;
                                btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
                                Log.i("Retweet", "onSuccess to publish tweet");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("Retweet", "onFailure to publish tweet", throwable);
                            }
                        });
                    }
                    else {
                        client.unRetweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                tweet.retweeted = false;
                                btnRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                                Log.i("Unretweet", "onSuccess to unretweet");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("Unretweet", "onFailure to unretweet", throwable);
                            }
                        });


                    }

                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!tweet.liked) {
                    client.favorite(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            tweet.liked = true;
                            btnLike.setImageResource(R.drawable.ic_vector_heart);
                            Log.i("Favorited", "onSuccess to publish tweet");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("Favorited", "onFailure to publish tweet", throwable);
                        }
                    }); }
                    else {
                        client.unfavorite(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                tweet.liked = false;
                                btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                                Log.i("Unfavorited", "onSuccess to publish tweet");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("Unfavorited", "onFailure to publish tweet", throwable);
                            }
                        });
                    }
                }
            });

        }

    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (ParseException e) {
            Log.i("huh", "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }


}
