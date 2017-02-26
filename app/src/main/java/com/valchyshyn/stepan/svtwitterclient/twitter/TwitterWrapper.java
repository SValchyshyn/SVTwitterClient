package com.valchyshyn.stepan.svtwitterclient.twitter;

import android.content.Context;

import com.valchyshyn.stepan.svtwitterclient.utilites.UserPreferences;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by valchyshyn on 25.02.17.
 */
public class TwitterWrapper {
  public static String consumerKey = "dgILcDCnVVT6lnw3hcHGc95vh";
  public static String consumerSecret = "Aeyga2nFFAuHJh0aX9Dp87GSgWBopTDlsfTsQ86CowexL8AJn6";

  public static Twitter getTwitterInstance(Context context) {
    ConfigurationBuilder cb = new ConfigurationBuilder();

    String OAuthToken = UserPreferences.getInstance(context).getPrefKeyOauthToken();
    String OAuthSecret = UserPreferences.getInstance(context).getPrefKeyOauthSecret();

    cb.setDebugEnabled(true)
        .setOAuthConsumerKey(consumerKey)
        .setOAuthConsumerSecret(consumerSecret)
        .setOAuthAccessToken(OAuthToken)
        .setOAuthAccessTokenSecret(OAuthSecret);
    TwitterFactory tf = new TwitterFactory(cb.build());
    return tf.getInstance();
  }

}
