package com.codepath.apps.restclienttemplate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private RecyclerView rvTweets;
    private TweetsAdapter adapter;
    private List<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        //Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //Initialize list of tweets and adapter from the data source
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        // Recycler view setup: layout manager and setting the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);
        Toast.makeText(this, "Success in Timeline!", Toast.LENGTH_SHORT).show();
        populateHomeTimeLine();
        Toast.makeText(this, "Success after populate in Home Timeline!", Toast.LENGTH_SHORT).show();
    }

    private void populateHomeTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("TwitterClient", response.toString());
                    List<Tweet> tweetsToAdd = new ArrayList<>();
                    //Iterate through the list of tweets

                for (int i = 0; i <response.length(); i++){
                    try {


                        //Convert each JSON object into a Tweet object
                        JSONObject jsonTweetObject = response.getJSONObject(i);
                        Tweet tweet = Tweet.fromJson(jsonTweetObject);
                        //Add  the tweet into our data source
                        tweets.add(tweet);
                        // Notify adapter
                        adapter.notifyItemInserted(tweets.size() - 1);

                    } catch (JSONException e) {
                        e.printStackTrace();


                    }


                }





            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("TwitterClient", errorResponse.toString());
            }
        });
    }
}
