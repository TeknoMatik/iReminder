package com.rg.ireminders.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.TimePicker
import com.rg.ireminders.R
import com.rg.ireminders.db.utils.TaskUtils
import java.util.Calendar

/**
 * Dialog for adding a reminder to a task's item
 */
class AddReminderDialogFragment : DialogFragment(), View.OnClickListener, DialogInterface.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var mCalendar: Calendar? = null
    private var mDateFormat: java.text.DateFormat? = null
    private var mTimeFormat: java.text.DateFormat? = null
    private var mDateButton: Button? = null
    private var mTimeButton: Button? = null
    private var mReminderCheckBox: CheckBox? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.add_reminder_dialog, null)

        mCalendar = Calendar.getInstance()
        mDateFormat = DateFormat.getMediumDateFormat(activity)
        mTimeFormat = DateFormat.getTimeFormat(activity)

        mDateButton = view.findViewById(R.id.dateButton) as Button
        mDateButton!!.text = mDateFormat!!.format(mCalendar!!.time)
        mDateButton!!.setOnClickListener(this)

        mTimeButton = view.findViewById(R.id.timeButton) as Button
        mTimeButton!!.text = mTimeFormat!!.format(mCalendar!!.time)
        mTimeButton!!.setOnClickListener(this)

        mReminderCheckBox = view.findViewById(R.id.reminderCheckBox) as CheckBox
        val hasReminder = arguments.getBoolean(HAS_REMINDER_ARG)
        mReminderCheckBox!!.isChecked = hasReminder

        builder.setView(view)
        builder.setPositiveButton(activity.getString(android.R.string.ok), this)
        builder.setNegativeButton(activity.getString(android.R.string.cancel), this)
        builder.setTitle(R.string.add_a_reminder_dialog_tittle)

        return builder.create()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dateButton -> {
                val year = mCalendar!!.get(Calendar.YEAR)
                val month = mCalendar!!.get(Calendar.MONTH)
                val day = mCalendar!!.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(activity, this, year, month, day)
                datePickerDialog.show()
            }
            R.id.timeButton -> {
                val hour = mCalendar!!.get(Calendar.HOUR_OF_DAY)
                val minute = mCalendar!!.get(Calendar.MINUTE)
                val is24HourFormat = DateFormat.is24HourFormat(activity)
                val timePickerDialog = TimePickerDialog(activity, this, hour, minute, is24HourFormat)
                timePickerDialog.show()
            }
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            val itemId = arguments.getLong(ITEM_ID_ARG)
            val listId = arguments.getLong(LIST_ID_ARG)
            if (mReminderCheckBox!!.isChecked) {
                TaskUtils.Factory.get(activity).addReminder(itemId, listId, mCalendar!!.timeInMillis)
            } else {
                TaskUtils.Factory.get(activity).removeReminder(itemId, listId)
            }
        }

        dialog.dismiss()
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        mCalendar!!.set(Calendar.YEAR, year)
        mCalendar!!.set(Calendar.MONTH, monthOfYear)
        mCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        mDateButton!!.text = mDateFormat!!.format(mCalendar!!.time)
        mReminderCheckBox!!.isChecked = true
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        mCalendar!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
        mCalendar!!.set(Calendar.MINUTE, minute)

        mTimeButton!!.text = mTimeFormat!!.format(mCalendar!!.time)
        mReminderCheckBox!!.isChecked = true
    }

    companion object {
        private val HAS_REMINDER_ARG = "hasReminderArg"
        private val ITEM_ID_ARG = "itemIdArg"
        private val LIST_ID_ARG = "listIdArg"

        fun newInstance(hasReminder: Boolean?, itemId: Long?, listId: Long?): AddReminderDialogFragment {
            val dialogFragment = AddReminderDialogFragment()

            val args = Bundle()
            args.putBoolean(HAS_REMINDER_ARG, hasReminder!!)
            args.putLong(ITEM_ID_ARG, itemId!!)
            args.putLong(LIST_ID_ARG, listId!!)
            dialogFragment.arguments = args

            return dialogFragment
        }
    }
}
