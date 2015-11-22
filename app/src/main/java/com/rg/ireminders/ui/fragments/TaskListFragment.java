package com.rg.ireminders.ui.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.entities.TaskList;
import com.rg.ireminders.ui.adapters.TaskListCursorAdapter;
import org.dmfs.provider.tasks.TaskContract;

/**
 * A task list fragment {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener{

  private static final int URL_LOADER = 0;
  private TaskListCursorAdapter mAdapter;
  private OnTaskListFragmentInteractionListener mListener;

  public TaskListFragment() {
    // Required empty public constructor
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task_list, container, false);

    mAdapter = new TaskListCursorAdapter(getActivity(), R.layout.list_item, null, 0);
    ListView listView = (ListView) view.findViewById(R.id.listView);
    listView.setAdapter(mAdapter);
    listView.setOnItemClickListener(this);

    getLoaderManager().initLoader(URL_LOADER, getActivity().getIntent().getExtras(), this);

    return view;
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (id == URL_LOADER) {
      Uri uri = TaskContract.TaskLists.getContentUri(TaskContract.AUTHORITY);
      String selection = "account_type != \"LOCAL\"";
      return new CursorLoader(getActivity(), uri, null, selection, null, null);
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

    if (mListener != null) {
      mListener.onTaskListItemClick(view, taskList.getName(), taskList.getId(), taskList.getColor());
    }
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnTaskListFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement OnTaskListFragmentInteractionListener");
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   */
  public interface OnTaskListFragmentInteractionListener {
    void onTaskListItemClick(View view, String taskName, Long taskId, Integer taskColor);
  }
}
