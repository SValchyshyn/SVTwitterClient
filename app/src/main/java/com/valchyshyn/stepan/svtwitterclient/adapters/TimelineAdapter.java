package com.valchyshyn.stepan.svtwitterclient.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.valchyshyn.stepan.svtwitterclient.R;
import com.valchyshyn.stepan.svtwitterclient.twitter.TwitterMentionsListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import twitter4j.Status;

/**
 * Created by valchyshyn on 25.02.17.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {


  private List<Status> tweets;
  private TwitterMentionsListener listener;

  public void setData(List<Status> tweets, TwitterMentionsListener listener) {
    this.tweets = tweets;
    this.listener = listener;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView handle;
    TextView tweet;
    TextView timeStamp;

    ViewHolder(View v) {
      super(v);
      view = v;
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);

    ViewHolder vh = new ViewHolder(v);
    vh.handle = (TextView) v.findViewById(R.id.user_handle);
    vh.tweet = (TextView) v.findViewById(R.id.tweet);
    vh.timeStamp = (TextView) v.findViewById(R.id.time_stamp);
    return vh;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

    if (tweets != null && !tweets.isEmpty()) {

      Status tweet = tweets.get(position);
      holder.handle.setText("@" + tweet.getUser().getScreenName());
      DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
      holder.timeStamp.setText(dateFormat.format(tweet.getCreatedAt()));
      holder.tweet.setText(tweet.getText());

      LinkBuilder.on(holder.tweet)
          .addLinks(getUserLinks())
          .build();
    }
  }

  private List<Link> getUserLinks() {
    List<Link> links = new ArrayList<>();

    Link mentions = new Link(Pattern.compile("@\\w{1,15}"));
    mentions.setTextColor(Color.parseColor("#00BCD4"));
    mentions.setHighlightAlpha(.4f);
    mentions.setUnderlined(false);
    mentions.setOnClickListener(new Link.OnClickListener() {
      @Override
      public void onClick(String clickedText) {
        listener.onMentionClicked(clickedText);
      }
    });

    links.add(mentions);
    return links;
  }

  @Override
  public int getItemCount() {
    if (tweets != null && !tweets.isEmpty()) {
      return tweets.size();
    } else {
      return 0;
    }
  }
}