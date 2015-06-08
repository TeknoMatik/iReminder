package com.rg.ireminders.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

public class DetailsCursorAdapter extends ResourceCursorAdapter {
  private static final String TAG = "DetailsAdapter";
  private Context mContext;
  private int mColor;
  private int mSelectionStart;
  private int mSelectionEnd;
  private EditText mLastTouched;

  private View.OnKeyListener mEditTextKeyListener = new View.OnKeyListener() {
    @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
        String title = ((EditText) v).getText().toString();
        Long id = (Long) v.getTag();
        TaskUtils.Factory.get(mContext).updateTask(id, title);
        return true;
      }
      return false;
    }
  };

  /**
   * Workaround for getting focus on edittext
   * Setting selection positions to temporary variables
   */
  private final View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
    @Override public void onFocusChange(View v, boolean hasFocus) {
      EditText editText = (EditText) v;
      mSelectionStart = editText.getSelectionStart();
      mSelectionEnd = editText.getSelectionEnd();
    }
  };

  /**
   * Workaround for getting focus on edittext
   * Setting edittext in temporary variable
   */
  private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
    @Override public boolean onTouch(View v, MotionEvent event) {
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
        mLastTouched = (EditText) v;
      }

      return false;
    }
  };

  private CompoundButton.OnCheckedChangeListener mOnCheckedChangedListener =
      new CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          Long id = (Long) buttonView.getTag();
          TaskUtilsImpl.Factory.get(mContext).changeTaskStatus(id, isChecked);
        }
      };

  public DetailsCursorAdapter(Context context, int layout, Cursor c, int flags, int color) {
    super(context, layout, c, flags);
    mContext = context;
    mColor = color;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    CheckBox statusCheckBox = (CheckBox) view.findViewById(R.id.radioButton);
    final EditText titleEditText = (EditText) view.findViewById(R.id.radioButtonText);
    TextView dueText = (TextView) view.findViewById(R.id.dueTextView);

    String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskColumns.TITLE));
    Integer status = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskColumns.STATUS));
    Long due = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.DUE));
    final Long id = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns._ID));

    Log.d(TAG, "bindView - " + title);

    statusCheckBox.setChecked(status != TaskContract.TaskColumns.STATUS_NEEDS_ACTION);
    statusCheckBox.setTag(id);
    statusCheckBox.setOnCheckedChangeListener(mOnCheckedChangedListener);
    setCheckBoxColor(statusCheckBox);

    titleEditText.setText(title);
    titleEditText.setTag(id);
    titleEditText.setOnKeyListener(mEditTextKeyListener);
    titleEditText.setOnFocusChangeListener(mOnFocusChangeListener);
    titleEditText.setOnTouchListener(mOnTouchListener);

    if (due == 0 || status == TaskContract.TaskColumns.STATUS_COMPLETED) {
      dueText.setVisibility(View.GONE);
    } else {
      String date = DateUtils.getDueDate(due);
      dueText.setVisibility(View.VISIBLE);
      dueText.setText(date);
    }

    //Return focus and selection position
    if (mLastTouched != null) {
      mLastTouched.requestFocus();
      mLastTouched.setSelection(mSelectionStart, mSelectionEnd);
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void setCheckBoxColor(CheckBox statusCheckBox) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      statusCheckBox.setButtonTintList(ColorStateList.valueOf(mColor));
    }
  }
}
