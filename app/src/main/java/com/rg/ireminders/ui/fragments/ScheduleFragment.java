package com.rg.ireminders.ui.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.entities.TaskItem;
import com.rg.ireminders.db.utils.TaskUtils;
import com.rg.ireminders.ui.adapters.ScheduleListAdapter;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScheduleFragment extends Fragment {

  public ScheduleFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_schedule, container, false);

    ListView listView = (ListView) view.findViewById(R.id.listView);
    List<TaskItem> taskItemList = TaskUtils.Factory.get(getActivity()).getScheduledTasks();
    ScheduleListAdapter adapter = new ScheduleListAdapter(getActivity(), taskItemList);
    listView.setAdapter(adapter);

    return view;
  }
}
