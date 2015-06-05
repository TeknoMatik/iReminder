package com.rg.ireminders.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import com.rg.ireminders.R;
import com.rg.ireminders.adapters.TaskListCursorAdapter;
import com.rg.ireminders.db.entities.TaskList;
import java.util.ArrayList;
import java.util.List;
import org.dmfs.provider.tasks.TaskContract;

public class TaskListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener{

  private static final int URL_LOADER = 0;
  private TaskListCursorAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mAdapter = new TaskListCursorAdapter(this, R.layout.list_item, null, 0);
    ListView listView = (ListView) findViewById(R.id.listView);
    listView.setAdapter(mAdapter);
    listView.setOnItemClickListener(this);
    getSupportLoaderManager().initLoader(URL_LOADER, getIntent().getExtras(), this);
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

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (id == URL_LOADER) {
      Uri uri = TaskContract.TaskLists.getContentUri(TaskContract.AUTHORITY);
      return new CursorLoader(this, uri, null, null, null, null);
    } else {
      return null;
    }
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mAdapter.changeCursor(data);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.changeCursor(null);
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Cursor cursor = (Cursor) mAdapter.getItem(position);
    TaskList taskList = new TaskList();
    taskList.fromCursor(cursor);

    Intent intent = new Intent(TaskListActivity.this, DetailsActivity.class);
    intent.putExtra(DetailsActivity.TASK_LIST_DETAILS_ARG, taskList.getName());
    intent.putExtra(DetailsActivity.TASK_LIST_ID_ARG, taskList.getId());
    intent.putExtra(DetailsActivity.TASK_LIST_COLOR_ARG, taskList.getColor());
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
