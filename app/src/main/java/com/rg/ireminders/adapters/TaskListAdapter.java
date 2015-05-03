package com.rg.ireminders.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.RadioButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.utils.DateUtils;
import org.dmfs.provider.tasks.TaskContract;

public class TaskListAdapter extends ResourceCursorAdapter {

  public TaskListAdapter(Context context, int layout, Cursor c, int flags) {
    super(context, layout, c, flags);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    RadioButton rb = (RadioButton) view.findViewById(R.id.radioButton);
    TextView rbText = (TextView) view.findViewById(R.id.radioButtonText);
    TextView dueText = (TextView) view.findViewById(R.id.dueTextView);
    String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskColumns.TITLE));
    Integer status = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskColumns.STATUS));
    Long due = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.DUE));
    rb.setChecked(status != TaskContract.TaskColumns.STATUS_NEEDS_ACTION);
    rbText.setText(title);
    if (due == 0 || status == TaskContract.TaskColumns.STATUS_COMPLETED) {
      dueText.setVisibility(View.GONE);
    } else {
      String date = DateUtils.getDueDate(due);
      dueText.setVisibility(View.VISIBLE);
      dueText.setText(date);
    }
  }
}
