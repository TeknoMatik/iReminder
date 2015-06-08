package com.rg.ireminders.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.entities.TaskList;

/**
 * Created by rustamgaifullin on 6/5/15.
 */
public class TaskListCursorAdapter extends ResourceCursorAdapter {

  public TaskListCursorAdapter(Context context, int layout, Cursor c, int flags) {
    super(context, layout, c, flags);
  }

  @Override public void bindView(View view, Context context, Cursor cursor) {
    TaskList taskList = new TaskList();
    taskList.fromCursor(cursor);
    TextView textView = (TextView) view.findViewById(R.id.info_text);
    textView.setText(taskList.getName());
    textView.setTextColor(taskList.getColor());
  }
}
