package com.rg.ireminders.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.utils.TaskUtils;
import com.rg.ireminders.db.utils.impl.TaskUtilsImpl;
import com.rg.ireminders.utils.DateUtils;
import org.dmfs.provider.tasks.TaskContract;

public class TaskItemsCursorAdapter extends ResourceCursorAdapter implements View.OnClickListener {
  private Context mContext;
  private int mColor;
  private Long mListId;

  private View.OnKeyListener mEditTextKeyListener = new View.OnKeyListener() {
    @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
        String title = ((EditText) v).getText().toString();
        Long id = (Long) v.getTag();
        TaskUtils.Factory.get(mContext).updateTask(id, mListId, title);
        return true;
      }
      return false;
    }
  };

  public TaskItemsCursorAdapter(Context context, int layout, Cursor c, int flags, int color, Long listId) {
    super(context, layout, c, flags);
    mContext = context;
    mColor = color;
    mListId = listId;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    final CheckBox statusCheckBox = (CheckBox) view.findViewById(R.id.radioButton);
    final EditText titleEditText = (EditText) view.findViewById(R.id.radioButtonText);
    TextView dueText = (TextView) view.findViewById(R.id.dueTextView);

    String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskColumns.TITLE));
    Integer status = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskColumns.STATUS));
    Long due = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.DUE));
    final Long id = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns._ID));

    statusCheckBox.setChecked(!status.equals(TaskContract.TaskColumns.STATUS_NEEDS_ACTION));
    statusCheckBox.setTag(id);
    statusCheckBox.setOnClickListener(this);
    setCheckBoxColor(statusCheckBox);

    titleEditText.setText(title);
    titleEditText.setTag(id);
    titleEditText.setOnKeyListener(mEditTextKeyListener);

    if (due == 0 || status == TaskContract.TaskColumns.STATUS_COMPLETED) {
      dueText.setVisibility(View.GONE);
    } else {
      String date = DateUtils.getDueDate(due);
      dueText.setVisibility(View.VISIBLE);
      dueText.setText(date);
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void setCheckBoxColor(CheckBox statusCheckBox) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      statusCheckBox.setButtonTintList(ColorStateList.valueOf(mColor));
    }
  }

  //On Click listener for a CheckBox
  @Override public void onClick(View v) {
    CheckBox statusCheckBox = (CheckBox) v;
    Long id = (Long) v.getTag();
    TaskUtilsImpl.Factory.get(mContext).changeTaskStatus(id, mListId, statusCheckBox.isChecked());
  }
}
