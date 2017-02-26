package com.valchyshyn.stepan.svtwitterclient.utilites;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.valchyshyn.stepan.svtwitterclient.activities.WebViewActivity;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static com.valchyshyn.stepan.svtwitterclient.twitter.TwitterWrapper.consumerKey;
import static com.valchyshyn.stepan.svtwitterclient.twitter.TwitterWrapper.consumerSecret;

/**
 * Created by valchyshyn on 26.02.17.
 */

public class TwitterClient {


  private static Twitter twitter;
  private static RequestToken requestToken;

  private static String callbackUrl = "oob";

  public static void initTwitterClient() {
    final ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.setOAuthConsumerKey(consumerKey);
    builder.setOAuthConsumerSecret(consumerSecret);

    final Configuration configuration = builder.build();
    final TwitterFactory factory = new TwitterFactory(configuration);
    twitter = factory.getInstance();
  }

  public static void verify(Context context, String pin) {
    try {
      AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
      saveAccessTokenDetails(context, accessToken);
    } catch (Exception e) {
      Log.e("Twitter Login Failed", e.getMessage());
    }
  }

  private static void saveAccessTokenDetails(Context context, AccessToken accessToken) {
    if (accessToken != null) {
      UserPreferences.getInstance(context).storeOauthToken(accessToken.getToken());
      UserPreferences.getInstance(context).storeOauthSecret(accessToken.getTokenSecret());
      UserPreferences.getInstance(context).storeUserTwitterLoggedIn(true);
    }
  }

  public static void openLoginView(Context context) {

    try {
      requestToken = twitter.getOAuthRequestToken(callbackUrl);

      final Intent intent = new Intent(context, WebViewActivity.class);
      intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
      context.startActivity(intent);

    } catch (TwitterException e) {
      e.printStackTrace();
    }
  }

  public static void postATweet(Context context, String text) {
    if (text.length() > 140) {
      Toast.makeText(context, "Tweet length cannot be more than 140 symbols", Toast.LENGTH_LONG).show();
    } else {
      try {
        twitter.setOAuthAccessToken(new AccessToken(UserPreferences.getInstance(context).getPrefKeyOauthToken(), UserPreferences.getInstance(context).getPrefKeyOauthSecret()));
        twitter.updateStatus(text);
        Toast.makeText(context, "Successfully tweeted", Toast.LENGTH_LONG).show();
      } catch (TwitterException e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        Log.e("Posting a tweet failed", e.getMessage());
      }
    }
  }
}
