package com.rg.ireminders.ui.adapters

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.database.Cursor
import android.os.Build
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ResourceCursorAdapter
import android.widget.TextView
import com.rg.ireminders.R
import com.rg.ireminders.db.entities.TaskItem
import com.rg.ireminders.db.utils.TaskUtils
import com.rg.ireminders.utils.DateUtils
import java.util.HashMap
import org.dmfs.provider.tasks.TaskContract

class TaskItemsCursorAdapter(private val mContext: Context, private val mTaskItemsAdapterListener: TaskItemsCursorAdapter.TaskItemsAdapterListener, layout: Int, c: Cursor?, flags: Int,
                             private val mColor: Int, private val mListId: Long?) : ResourceCursorAdapter(mContext, layout, c, flags), View.OnClickListener {
    private var mLastFocusedButton: Button? = null
    private val mStatusMap = HashMap<Long, Boolean>()

    private val mEditTextKeyListener = View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
            val title = (v as EditText).text.toString()
            val id = (v.tag as TaskItem).id
            TaskUtils.Factory.get(mContext).updateTask(id, mListId, title)
            return@OnKeyListener true
        }
        false
    }

    private val mOnTouchListener = View.OnTouchListener { v, event ->
        val button = v.getTag(R.id.addReminderButton) as Button
        if (event.action == MotionEvent.ACTION_UP) {
            if (button !== mLastFocusedButton) {
                if (mLastFocusedButton != null) {
                    mLastFocusedButton!!.visibility = View.GONE
                }
                mLastFocusedButton = button
                button.visibility = View.VISIBLE
            }
        }

        false
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val statusCheckBox = view.findViewById(R.id.statusCheckBox) as CheckBox
        val titleEditText = view.findViewById(R.id.titleEditText) as EditText
        val dueText = view.findViewById(R.id.dueTextView) as TextView
        val addReminderButton = view.findViewById(R.id.addReminderButton) as Button

        val taskItem = TaskItem()
        taskItem.fromCursor(cursor)

        val title = taskItem.title
        val status = taskItem.status
        val due = taskItem.due

        if (mStatusMap.containsKey(taskItem.id!!)) {
            val newStatus = mStatusMap[taskItem.id!!]
            statusCheckBox.isChecked = newStatus!!
        } else {
            statusCheckBox.isChecked = status != TaskContract.TaskColumns.STATUS_NEEDS_ACTION
        }
        statusCheckBox.tag = taskItem
        statusCheckBox.setOnClickListener(this)
        setCheckBoxColor(statusCheckBox)

        titleEditText.setText(title)
        titleEditText.tag = taskItem
        titleEditText.setTag(R.id.addReminderButton, addReminderButton)
        titleEditText.setOnKeyListener(mEditTextKeyListener)
        titleEditText.setOnTouchListener(mOnTouchListener)

        addReminderButton.tag = taskItem
        addReminderButton.setTag(R.id.titleEditText, titleEditText)
        addReminderButton.setOnClickListener(this)

        if (due === 0L || status === TaskContract.TaskColumns.STATUS_COMPLETED) {
            dueText.visibility = View.GONE
        } else {
            val date = DateUtils.getDueDateTime(due)
            dueText.visibility = View.VISIBLE
            dueText.text = date
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setCheckBoxColor(statusCheckBox: CheckBox) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusCheckBox.buttonTintList = ColorStateList.valueOf(mColor)
        }
    }

    override fun onClick(v: View) {
        val taskItem = v.tag as TaskItem
        when (v.id) {
            R.id.statusCheckBox -> {
                val statusCheckBox = v as CheckBox
                mStatusMap.put(taskItem.id!!, statusCheckBox.isChecked)
                TaskUtils.Factory.get(mContext).changeTaskStatus(taskItem.id, mListId, statusCheckBox.isChecked)
            }
            R.id.addReminderButton -> {
                val editText = v.getTag(R.id.titleEditText) as EditText
                if (editText != null) {
                    hideKeyboard(editText)
                }

                mLastFocusedButton!!.visibility = View.GONE
                mLastFocusedButton = null
                mTaskItemsAdapterListener.onAddReminder(taskItem.due !== 0L, taskItem.id, taskItem.listId)
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    public interface TaskItemsAdapterListener {
        fun onAddReminder(hasReminder: Boolean?, itemId: Long?, listId: Long?)
    }
}
