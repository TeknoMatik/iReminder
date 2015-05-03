package com.rg.ireminders.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.utils.TaskUtils;
import com.rg.ireminders.utils.DateUtils;
import org.dmfs.provider.tasks.TaskContract;

public class TaskListAdapter extends ResourceCursorAdapter {
  private Context mContext;

  private View.OnKeyListener mEditTextKeyListener = new View.OnKeyListener() {
    @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
        updateItem(((EditText) v).getText().toString(), (Long) v.getTag());
        return true;
      }
      return false;
    }
  };

  private View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      v.setFocusable(true);
    }
  };

  public TaskListAdapter(Context context, int layout, Cursor c, int flags) {
    super(context, layout, c, flags);
    mContext = context;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    RadioButton rb = (RadioButton) view.findViewById(R.id.radioButton);
    EditText rbText = (EditText) view.findViewById(R.id.radioButtonText);
    TextView dueText = (TextView) view.findViewById(R.id.dueTextView);
    String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskColumns.TITLE));
    Integer status = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskColumns.STATUS));
    Long due = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.DUE));
    rb.setChecked(status != TaskContract.TaskColumns.STATUS_NEEDS_ACTION);
    rbText.setText(title);
    rbText.setTag(cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns._ID)));
    rbText.setOnKeyListener(mEditTextKeyListener);
    rbText.setOnClickListener(mOnClickListener);
    if (due == 0 || status == TaskContract.TaskColumns.STATUS_COMPLETED) {
      dueText.setVisibility(View.GONE);
    } else {
      String date = DateUtils.getDueDate(due);
      dueText.setVisibility(View.VISIBLE);
      dueText.setText(date);
    }
  }



  private void updateItem(String title, Long id) {
    TaskUtils.Factory.get(mContext).updateTask(id, title);
  }
}
