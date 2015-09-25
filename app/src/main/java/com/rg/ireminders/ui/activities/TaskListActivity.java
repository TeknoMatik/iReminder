package com.rg.ireminders.ui.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import com.rg.ireminders.R;
import com.rg.ireminders.ui.fragments.TaskListFragment;
import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends BaseActivity implements TaskListFragment.OnTaskListFragmentInteractionListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected int getLayoutResource() {
    return R.layout.activity_list;
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

  @Override public void onTaskListItemClick(View view, String taskName, Long taskId, Integer taskColor) {
    Intent intent = new Intent(TaskListActivity.this, TaskItemsActivity.class);
    intent.putExtra(TaskItemsActivity.TASK_LIST_DETAILS_ARG, taskName);
    intent.putExtra(TaskItemsActivity.TASK_LIST_ID_ARG, taskId);
    intent.putExtra(TaskItemsActivity.TASK_LIST_COLOR_ARG, taskColor);
    View transitionView = view.findViewById(R.id.transition_view);
    List<Pair<View, String>> pairs = createPairs(transitionView);

    ActivityOptionsCompat activityOptions =
        ActivityOptionsCompat.makeSceneTransitionAnimation(TaskListActivity.this, pairs.toArray(new Pair[pairs.size()]));
    ActivityCompat.startActivity(TaskListActivity.this, intent, activityOptions.toBundle());
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
}
