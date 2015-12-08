package com.rg.ireminders.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import com.rg.ireminders.R;

public class ScheduleActivity extends BaseActivity{

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override protected int getLayoutResource() {
    return R.layout.activity_schedule;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        supportFinishAfterTransition();
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
