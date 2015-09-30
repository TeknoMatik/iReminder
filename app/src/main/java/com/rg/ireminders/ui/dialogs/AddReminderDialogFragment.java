package com.rg.ireminders.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.rg.ireminders.R;
import com.rg.ireminders.db.utils.TaskUtils;
import java.util.Calendar;

/**
 * Dialog for adding a reminder to a task's item
 */
public class AddReminderDialogFragment extends DialogFragment implements View.OnClickListener,
    DialogInterface.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
  private static final String HAS_REMINDER_ARG = "hasReminderArg";
  private static final String ITEM_ID_ARG = "itemIdArg";
  private static final String LIST_ID_ARG = "listIdArg";

  private Calendar mCalendar;
  private java.text.DateFormat mDateFormat;
  private java.text.DateFormat mTimeFormat;
  private Button mDateButton;
  private Button mTimeButton;
  private CheckBox mReminderCheckBox;

  public static AddReminderDialogFragment newInstance(Boolean hasReminder, Long itemId, Long listId) {
    AddReminderDialogFragment dialogFragment = new AddReminderDialogFragment();

    Bundle args = new Bundle();
    args.putBoolean(HAS_REMINDER_ARG, hasReminder);
    args.putLong(ITEM_ID_ARG, itemId);
    args.putLong(LIST_ID_ARG, listId);
    dialogFragment.setArguments(args);

    return dialogFragment;
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.add_reminder_dialog, null);

    mCalendar = Calendar.getInstance();
    mDateFormat = DateFormat.getMediumDateFormat(getActivity());
    mTimeFormat = DateFormat.getTimeFormat(getActivity());

    mDateButton = (Button) view.findViewById(R.id.dateButton);
    mDateButton.setText(mDateFormat.format(mCalendar.getTime()));
    mDateButton.setOnClickListener(this);

    mTimeButton = (Button) view.findViewById(R.id.timeButton);
    mTimeButton.setText(mTimeFormat.format(mCalendar.getTime()));
    mTimeButton.setOnClickListener(this);

    mReminderCheckBox = (CheckBox) view.findViewById(R.id.reminderCheckBox);
    Boolean hasReminder = getArguments().getBoolean(HAS_REMINDER_ARG);
    mReminderCheckBox.setChecked(hasReminder);

    builder.setView(view);
    builder.setPositiveButton(getActivity().getString(android.R.string.ok), this);
    builder.setNegativeButton(getActivity().getString(android.R.string.cancel), this);
    builder.setTitle(R.string.add_a_reminder_dialog_tittle);

    return builder.create();
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.dateButton:
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.show();
        break;
      case R.id.timeButton:
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(getActivity());
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, is24HourFormat);
        timePickerDialog.show();
        break;
    }
  }

  @Override public void onClick(DialogInterface dialog, int which) {
    if (which == DialogInterface.BUTTON_POSITIVE) {
      Long itemId = getArguments().getLong(ITEM_ID_ARG);
      Long listId = getArguments().getLong(LIST_ID_ARG);
      if (mReminderCheckBox.isChecked()) {
        TaskUtils.Factory.get(getActivity()).addReminder(itemId, listId, mCalendar.getTimeInMillis());
      } else {
        TaskUtils.Factory.get(getActivity()).removeReminder(itemId, listId);
      }
    }

    dialog.dismiss();
  }

  @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    mCalendar.set(Calendar.YEAR, year);
    mCalendar.set(Calendar.MONTH, monthOfYear);
    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

    mDateButton.setText(mDateFormat.format(mCalendar.getTime()));
    mReminderCheckBox.setChecked(true);
  }

  @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
    mCalendar.set(Calendar.MINUTE, minute);

    mTimeButton.setText(mTimeFormat.format(mCalendar.getTime()));
    mReminderCheckBox.setChecked(true);
  }
}
