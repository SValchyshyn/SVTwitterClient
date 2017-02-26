package com.valchyshyn.stepan.svtwitterclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.valchyshyn.stepan.svtwitterclient.activities.TimelineActivity;
import com.valchyshyn.stepan.svtwitterclient.utilites.TwitterClient;
import com.valchyshyn.stepan.svtwitterclient.utilites.UserPreferences;
import com.valchyshyn.stepan.svtwitterclient.utilites.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.login_button)
  ImageButton button;

  @BindView(R.id.pin_field)
  EditText pinField;

  private Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(R.anim.slide_down, R.anim.abc_fade_out);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    context = this;

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
    TwitterClient.initTwitterClient();

    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!Utilities.isNetworkAvailable(context)) {
          Toast.makeText(context, context.getResources().getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
        } else {
          performInitTasks();
        }
      }
    });
  }

  private void performInitTasks() {
    if (UserPreferences.getInstance(getApplicationContext()).isLoggedIn()) {
      Intent searchViewIntent = new Intent(this, TimelineActivity.class);
      startActivity(searchViewIntent);
    } else {
      if (pinField.getText().toString().length() != 0) {
        String verifyPin = pinField.getText().toString();
        TwitterClient.verify(getApplicationContext(), verifyPin);
      } else {
        TwitterClient.openLoginView(this);
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
