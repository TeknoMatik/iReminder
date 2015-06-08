package com.rg.ireminders.ui.activities;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.ui.adapters.DetailsCursorAdapter;
import com.rg.ireminders.db.utils.TaskUtils;
import java.util.Date;
import org.dmfs.provider.tasks.TaskContract;

public class DetailsActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  public static final String TASK_LIST_ID_ARG = "taskListId";
  public static final String TASK_LIST_DETAILS_ARG = "taskListDetails";
  public static final String TASK_LIST_COLOR_ARG = "taskListColor";

  private static final int URL_LOADER = 0;
  private DetailsCursorAdapter mAdapter;
  private Boolean mShowHidden = false;
  private EditText mAddEditText;
  private Long mListId;
  private Long mShowTime;

  private View.OnKeyListener mAddEditTextKeyListener = new View.OnKeyListener() {
    @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
        insertItem();
        return true;
      }
      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mShowTime = new Date().getTime();
    onCreateLolipop();

    int color = getIntent().getIntExtra(TASK_LIST_COLOR_ARG, 0);
    String detailString = getIntent().getStringExtra(TASK_LIST_DETAILS_ARG);
    mListId = getIntent().getLongExtra(TASK_LIST_ID_ARG, 0);
    TextView mTextView = (TextView) findViewById(R.id.text_view);
    mTextView.setText(detailString);
    mTextView.setTextColor(color);

    mAdapter = new DetailsCursorAdapter(this, R.layout.details_item, null, 0, color);
    ListView mListView = (ListView) findViewById(R.id.task_list);
    mListView.setAdapter(mAdapter);
    getSupportLoaderManager().initLoader(URL_LOADER, getIntent().getExtras(), this);

    RelativeLayout footerLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.details_item_add, null);
    mAddEditText = (EditText) footerLayout.findViewById(R.id.addTaskEditText);
    mAddEditText.setOnKeyListener(mAddEditTextKeyListener);
    mListView.addFooterView(footerLayout);

    //mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    //mListView.setStackFromBottom(true);
  }

  /**
   * This method prevent status bar from blinking
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void onCreateLolipop() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      postponeEnterTransition();
      final ViewTreeObserver observer = getWindow().getDecorView().getViewTreeObserver();
      observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
          if (observer.isAlive()) {
            observer.removeOnPreDrawListener(this);
          }
          startPostponedEnterTransition();
          return true;
        }
      });
    }
  }

  @Override protected void onResume() {
    super.onResume();
  }

  @Override
  protected int getLayoutResource() {
    return R.layout.activity_details;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_details, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case android.R.id.home:
        supportFinishAfterTransition();
        return true;
      case R.id.show_hidden: {
        mShowHidden = !item.isChecked();
        item.setChecked(mShowHidden);
        refreshList();
        return true;
      }
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (id == URL_LOADER) {
      Long taskListId = args.getLong(TASK_LIST_ID_ARG);
      String selection;

      if (mShowHidden) {
        selection = String.format("%s = %d", TaskContract.TaskColumns.LIST_ID, taskListId);
      } else {
        selection = String.format("%s = %d AND %s = %d OR (%s == %d AND %s > %d)",
            TaskContract.TaskColumns.LIST_ID, taskListId,
            TaskContract.TaskColumns.STATUS, TaskContract.TaskColumns.STATUS_DEFAULT,
            TaskContract.TaskColumns.STATUS, TaskContract.TaskColumns.STATUS_COMPLETED,
            TaskContract.TaskColumns.COMPLETED, mShowTime);
      }

      return new CursorLoader(this, TaskContract.Tasks.CONTENT_URI, null, selection, null,
          TaskContract.TaskColumns.STATUS + " ASC" + ", " + TaskContract.TaskColumns.CREATED + " ASC");
    } else {
      return null;
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mAdapter.changeCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.changeCursor(null);
  }

  public void textClick(View view) {
    supportFinishAfterTransition();
  }

  private void refreshList() {
    getSupportLoaderManager().restartLoader(URL_LOADER, getIntent().getExtras(), this);
    mAdapter.notifyDataSetChanged();
  }

  private void insertItem() {
    String title = mAddEditText.getText().toString();
    Boolean isInserted = TaskUtils.Factory.get(this).insertTask(title, mListId);
    if (isInserted) {
      mAddEditText.setText("");
      mAddEditText.setFocusable(true);
    }
  }
}
