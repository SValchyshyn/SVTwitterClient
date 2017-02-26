package com.valchyshyn.stepan.svtwitterclient.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.valchyshyn.stepan.svtwitterclient.R;
import com.valchyshyn.stepan.svtwitterclient.adapters.TimelineAdapter;
import com.valchyshyn.stepan.svtwitterclient.asyncTasks.TwitterSearchFetch;
import com.valchyshyn.stepan.svtwitterclient.constants.Constants;
import com.valchyshyn.stepan.svtwitterclient.twitter.TwitterMentionsListener;
import com.valchyshyn.stepan.svtwitterclient.twitter.TwitterSearchRequester;
import com.valchyshyn.stepan.svtwitterclient.utilites.TwitterClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitter4j.Status;

/**
 * Created by valchyshyn on 25.02.17.
 */
public class TimelineActivity extends AppCompatActivity implements TwitterSearchRequester, TwitterMentionsListener {

  @BindView(R.id.result_recycler)
  RecyclerView mRecyclerView;

  @BindView(R.id.progress)
  CircularProgressView progressView;

  @BindView(R.id.fab_tweet)
  FloatingActionButton fabTweet;

  private TimelineAdapter searchAdapter;
  private TwitterSearchFetch searchFetchTask;
  private Context context = this;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    overridePendingTransition(R.anim.slide_up, R.anim.abc_fade_out);
    setContentView(R.layout.timeline_activity);
    ButterKnife.bind(this);

    searchAdapter = new TimelineAdapter();
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(searchAdapter);

    searchFetchTask = new TwitterSearchFetch(this, TimelineActivity.this);
    searchFetchTask.execute(Constants.DEFAULT_USER_TIMELINE);

    fabTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getTweetDialog().show();
      }
    });
  }

  private AlertDialog getTweetDialog() {
    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
    View dialogView = layoutInflaterAndroid.inflate(R.layout.tweet_input_dialog, null);
    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
    alertDialogBuilderUserInput.setView(dialogView);

    final EditText userInputDialogEditText = (EditText) dialogView.findViewById(R.id.userInputDialog);
    alertDialogBuilderUserInput
        .setCancelable(false)
        .setPositiveButton("Tweet", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogBox, int id) {
            TwitterClient.postATweet(context, userInputDialogEditText.getText().toString());
          }
        })

        .setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialogBox, int id) {
                dialogBox.cancel();
              }
            });

    return alertDialogBuilderUserInput.create();
  }


  void enterReveal(View myView) {

    // get the center for the clipping circle
    int cx = myView.getMeasuredWidth() / 2;
    int cy = myView.getMeasuredHeight() / 2;

    // get the final radius for the clipping circle
    int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // create the animator for this view (the start radius is zero)
      Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

      // make the view visible and start the animation
      myView.setVisibility(View.VISIBLE);
      anim.start();
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_search_activity, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void finishSuccess(List<Status> statuses) {
    searchAdapter.setData(statuses, this);
    searchAdapter.notifyDataSetChanged();
    enterReveal(mRecyclerView);
    searchFetchTask.cancel(true);
    stopProgress();
  }

  private void startProgress() {
    progressView.setVisibility(View.VISIBLE);
    progressView.startAnimation();
  }

  private void stopProgress() {
    progressView.setVisibility(View.GONE);
  }

  @Override
  public void onMentionClicked(String mention) {
    searchFetchTask = new TwitterSearchFetch(this, TimelineActivity.this);
    searchFetchTask.execute(mention);
    startProgress();
  }
}
