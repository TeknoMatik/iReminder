package com.rg.ireminders.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
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

public class DetailsCursorAdapter extends ResourceCursorAdapter {
  private static final String TAG = "DetailsAdapter";
  private Context mContext;
  private int mColor;

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

  private View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      //v.requestFocus();
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
    titleEditText.setOnClickListener(mOnClickListener);

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
}
