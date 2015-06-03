package com.rg.ireminders.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import com.rg.ireminders.R;
import com.rg.ireminders.adapters.TaskListAdapter;
import com.rg.ireminders.db.entities.TaskList;
import com.rg.ireminders.db.utils.TaskUtils;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

  TaskListAdapter.IReminderClickListener listener = new TaskListAdapter.IReminderClickListener() {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP) @Override
    public void onClick(View v, String item, Long taskId, int color) {
      Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
      intent.putExtra(DetailsActivity.TASK_LIST_DETAILS_ARG, item);
      intent.putExtra(DetailsActivity.TASK_LIST_ID_ARG, taskId);
      intent.putExtra(DetailsActivity.TASK_LIST_COLOR_ARG, color);
      List<Pair<View, String>> pairs = createPairs(v);

      ActivityOptionsCompat activityOptions =
          ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pairs.toArray(new Pair[pairs.size()]));
      ActivityCompat.startActivity(MainActivity.this, intent, activityOptions.toBundle());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private List<Pair<View, String>> createPairs(View v) {
      List<Pair<View, String>> pairs = new ArrayList<>();
      View navigationBarBackground = findViewById(android.R.id.navigationBarBackground);
      if (navigationBarBackground != null) {
        pairs.add(Pair.create(navigationBarBackground, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
      }

      View statusBarBackground = findViewById(android.R.id.statusBarBackground);
      if (statusBarBackground != null) {
        pairs.add(Pair.create(statusBarBackground, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
      }

      if (toolbar != null) {
        pairs.add(Pair.create((View) toolbar, "toolbar"));
      }
      if (v != null) {
        pairs.add(Pair.create(v, "item"));
      }

      return pairs;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TaskUtils mTaskListUtils = TaskUtils.Factory.get(this);
    List<TaskList> taskList = mTaskListUtils.getTaskList();

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    mRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);

    RecyclerView.Adapter mAdapter = new TaskListAdapter(taskList, listener);
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override
  protected int getLayoutResource() {
    return R.layout.activity_main;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    return id == R.id.action_settings || super.onOptionsItemSelected(item);
  }
}
