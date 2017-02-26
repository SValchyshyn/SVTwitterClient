package com.valchyshyn.stepan.svtwitterclient.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.valchyshyn.stepan.svtwitterclient.twitter.TwitterSearchRequester;
import com.valchyshyn.stepan.svtwitterclient.twitter.TwitterWrapper;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by valchyshyn on 26.02.17.
 */
public class TwitterSearchFetch extends AsyncTask<String, Void, List<Status>> {

  private Context context;
  private TwitterSearchRequester requester;

  public TwitterSearchFetch(Context context, TwitterSearchRequester requester) {
    this.context = context;
    this.requester = requester;
  }

  @Override
  protected List<twitter4j.Status> doInBackground(String... params) {
    Twitter twitter = TwitterWrapper.getTwitterInstance(context.getApplicationContext());
    List<twitter4j.Status> tweets = null;
    try {
      tweets = twitter.getUserTimeline(params[0]);

    } catch (TwitterException te) {
      te.printStackTrace();
      System.out.println("Failed to search tweets: " + te.getMessage());
    }
    return tweets;
  }

  @Override
  protected void onPostExecute(List<twitter4j.Status> statuses) {
    super.onPostExecute(statuses);
    requester.finishSuccess(statuses);
  }
}
