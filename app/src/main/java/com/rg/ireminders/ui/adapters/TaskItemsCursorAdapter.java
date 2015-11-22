package com.rg.ireminders.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.entities.TaskItem;
import com.rg.ireminders.db.utils.TaskUtils;
import com.rg.ireminders.db.utils.impl.TaskUtilsImpl;
import com.rg.ireminders.utils.DateUtils;
import java.util.HashMap;
import java.util.Map;
import org.dmfs.provider.tasks.TaskContract;

public class TaskItemsCursorAdapter extends ResourceCursorAdapter implements View.OnClickListener {
  private Context mContext;
  private int mColor;
  private Long mListId;
  private Button mLastFocusedButton;
  private TaskItemsAdapterListener mTaskItemsAdapterListener;
  private Map<Long, Boolean> mStatusMap = new HashMap<>();

  private View.OnKeyListener mEditTextKeyListener = new View.OnKeyListener() {
    @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
        String title = ((EditText) v).getText().toString();
        Long id = ((TaskItem) v.getTag()).getId();
        TaskUtils.Factory.get(mContext).updateTask(id, mListId, title);
        return true;
      }
      return false;
    }
  };

  private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
    @Override public boolean onTouch(View v, MotionEvent event) {
      Button button = (Button) v.getTag(R.id.addReminderButton);
      if (event.getAction() == MotionEvent.ACTION_UP) {
        if (button != mLastFocusedButton) {
          if (mLastFocusedButton != null) {
            mLastFocusedButton.setVisibility(View.GONE);
          }
          mLastFocusedButton = button;
          button.setVisibility(View.VISIBLE);
        }
      }

      return false;
    }
  };

  public TaskItemsCursorAdapter(Context context, TaskItemsAdapterListener taskItemsAdapterListener, int layout, Cursor c, int flags,
      int color, Long listId) {
    super(context, layout, c, flags);
    mContext = context;
    mColor = color;
    mListId = listId;
    mTaskItemsAdapterListener = taskItemsAdapterListener;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    final CheckBox statusCheckBox = (CheckBox) view.findViewById(R.id.statusCheckBox);
    final EditText titleEditText = (EditText) view.findViewById(R.id.titleEditText);
    TextView dueText = (TextView) view.findViewById(R.id.dueTextView);
    Button addReminderButton = (Button) view.findViewById(R.id.addReminderButton);

    TaskItem taskItem = new TaskItem();
    taskItem.fromCursor(cursor);

    String title = taskItem.getTitle();
    Integer status = taskItem.getStatus();
    Long due = taskItem.getDue();

    if (mStatusMap.containsKey(taskItem.getId())) {
      Boolean newStatus = mStatusMap.get(taskItem.getId());
      statusCheckBox.setChecked(newStatus);
    } else {
      statusCheckBox.setChecked(!status.equals(TaskContract.TaskColumns.STATUS_NEEDS_ACTION));
    }
    statusCheckBox.setTag(taskItem);
    statusCheckBox.setOnClickListener(this);
    setCheckBoxColor(statusCheckBox);

    titleEditText.setText(title);
    titleEditText.setTag(taskItem);
    titleEditText.setTag(R.id.addReminderButton, addReminderButton);
    titleEditText.setOnKeyListener(mEditTextKeyListener);
    titleEditText.setOnTouchListener(mOnTouchListener);

    addReminderButton.setTag(taskItem);
    addReminderButton.setTag(R.id.titleEditText, titleEditText);
    addReminderButton.setOnClickListener(this);

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

  @Override public void onClick(View v) {
    TaskItem taskItem = (TaskItem) v.getTag();
    switch (v.getId()) {
      case R.id.statusCheckBox :
        CheckBox statusCheckBox = (CheckBox) v;
        mStatusMap.put(taskItem.getId(), statusCheckBox.isChecked());
        mTaskItemsAdapterListener.onStatusChanged(statusCheckBox.isChecked(), taskItem.getId());
        break;
      case R.id.addReminderButton :
        EditText editText = (EditText) v.getTag(R.id.titleEditText);
        if (editText != null) {
          hideKeyboard(editText);
        }

        mLastFocusedButton.setVisibility(View.GONE);
        mLastFocusedButton = null;
        mTaskItemsAdapterListener.onAddReminder(taskItem.getDue() != 0, taskItem.getId(), taskItem.getListId());
        break;
    }
  }

  private void hideKeyboard(View view) {
    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public interface TaskItemsAdapterListener {
    void onAddReminder(Boolean hasReminder, Long itemId, Long listId);
    void onStatusChanged(Boolean status, Long itemId);
  }
}
