package com.rg.ireminders.ui.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.utils.TaskUtils;
import com.rg.ireminders.ui.activities.TaskItemsActivity;
import com.rg.ireminders.ui.adapters.TaskItemsCursorAdapter;
import com.rg.ireminders.ui.dialogs.AddReminderDialogFragment;
import java.util.Date;
import org.dmfs.provider.tasks.TaskContract;

/**
 * A task items fragment {@link Fragment} subclass.
 */
public class TaskItemsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {
  private static final int URL_LOADER = 0;
  private TaskItemsCursorAdapter mAdapter;
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

  private TaskItemsCursorAdapter.OnAddReminderClick mOnAddReminderClick =
      new TaskItemsCursorAdapter.OnAddReminderClick() {
        @Override public void onClick(Boolean hasReminder, Long itemId, Long listId) {
          DialogFragment dateDialogFragment = AddReminderDialogFragment.newInstance(hasReminder, itemId, listId);
          getFragmentManager().beginTransaction().add(dateDialogFragment, "addReminderDialog").commit();
        }
      };

  public TaskItemsFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task_items, container, false);

    mShowTime = new Date().getTime();

    int color = getActivity().getIntent().getIntExtra(TaskItemsActivity.TASK_LIST_COLOR_ARG, 0);
    String detailString = getActivity().getIntent().getStringExtra(TaskItemsActivity.TASK_LIST_DETAILS_ARG);
    mListId = getActivity().getIntent().getLongExtra(TaskItemsActivity.TASK_LIST_ID_ARG, 0);
    TextView mTextView = (TextView) view.findViewById(R.id.text_view);
    mTextView.setText(detailString);
    mTextView.setTextColor(color);

    mAdapter = new TaskItemsCursorAdapter(getActivity(), mOnAddReminderClick, R.layout.task_item, null, 0, color, mListId);
    ListView mListView = (ListView) view.findViewById(R.id.task_list);
    mListView.setAdapter(mAdapter);
    getLoaderManager().initLoader(URL_LOADER, getActivity().getIntent().getExtras(), this);

    GridLayout footerLayout = (GridLayout) getLayoutInflater(savedInstanceState).inflate(R.layout.task_item_add, null);
    mAddEditText = (EditText) footerLayout.findViewById(R.id.addTaskEditText);
    mAddEditText.setOnKeyListener(mAddEditTextKeyListener);
    mListView.addFooterView(footerLayout);
    mListView.setClickable(false);

    setHasOptionsMenu(true);

    return view;
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (id == URL_LOADER) {
      Long taskListId = args.getLong(TaskItemsActivity.TASK_LIST_ID_ARG);
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

      return new CursorLoader(getActivity(), TaskContract.Tasks.CONTENT_URI, null, selection, null,
          TaskContract.TaskColumns.STATUS + " ASC" + ", " + TaskContract.TaskColumns.CREATED + " ASC");
    } else {
      return null;
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.swapCursor(null);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_details, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case android.R.id.home:
        getActivity().supportFinishAfterTransition();
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

  private void insertItem() {
    String title = mAddEditText.getText().toString();
    Boolean isInserted = TaskUtils.Factory.get(getActivity()).insertTask(title, mListId);
    if (isInserted) {
      mAddEditText.setText("");
      mAddEditText.setFocusable(true);
    }
  }

  private void refreshList() {
    getLoaderManager().restartLoader(URL_LOADER, getActivity().getIntent().getExtras(), this);
    mAdapter.notifyDataSetChanged();
  }
}
