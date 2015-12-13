package com.rg.ireminders.ui.fragments

import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import android.widget.EditText
import android.widget.GridLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.rg.ireminders.R
import com.rg.ireminders.db.utils.TaskUtils
import com.rg.ireminders.ui.activities.TaskItemsActivity
import com.rg.ireminders.ui.adapters.TaskItemsCursorAdapter
import com.rg.ireminders.ui.dialogs.AddReminderDialogFragment
import java.util.Date
import java.util.HashMap
import org.dmfs.provider.tasks.TaskContract

/**
 * A task items fragment [Fragment] subclass.
 */
class TaskItemsFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>, TaskItemsCursorAdapter.TaskItemsAdapterListener {
    private var mAdapter: TaskItemsCursorAdapter? = null
    private var mShowHidden: Boolean? = false
    private var mAddEditText: EditText? = null
    private var mListId: Long? = null
    private var mShowTime: Long? = null

    private val mAddEditTextKeyListener = View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
            insertItem()
            return@OnKeyListener true
        }
        false
    }

    override fun onAddReminder(hasReminder: Boolean?, itemId: Long?, listId: Long?) {
        val dateDialogFragment = AddReminderDialogFragment.newInstance(hasReminder, itemId, listId)
        fragmentManager.beginTransaction().add(dateDialogFragment, "addReminderDialog").commit()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_task_items, container, false)

        mShowTime = Date().time

        val color = activity.intent.getIntExtra(TaskItemsActivity.TASK_LIST_COLOR_ARG, 0)
        val detailString = activity.intent.getStringExtra(TaskItemsActivity.TASK_LIST_DETAILS_ARG)
        mListId = activity.intent.getLongExtra(TaskItemsActivity.TASK_LIST_ID_ARG, 0)
        val mTextView = view.findViewById(R.id.text_view) as TextView
        mTextView.text = detailString
        mTextView.setTextColor(color)

        mAdapter = TaskItemsCursorAdapter(activity, this, R.layout.task_item, null, 0, color, mListId)
        val mListView = view.findViewById(R.id.task_list) as ListView
        mListView.adapter = mAdapter
        loaderManager.initLoader(URL_LOADER, activity.intent.extras, this)

        val footerLayout = getLayoutInflater(savedInstanceState).inflate(R.layout.task_item_add, null) as GridLayout
        mAddEditText = footerLayout.findViewById(R.id.addTaskEditText) as EditText
        mAddEditText!!.setOnKeyListener(mAddEditTextKeyListener)
        mListView.addFooterView(footerLayout)
        mListView.isClickable = false

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor>? {
        if (id == URL_LOADER) {
            val taskListId = args.getLong(TaskItemsActivity.TASK_LIST_ID_ARG)
            val selection: String
            val sorting: String

            val columnListId = TaskContract.TaskColumns.LIST_ID

            if (mShowHidden!!) {
                selection = "$columnListId = $taskListId"
                sorting = TaskContract.TaskColumns.STATUS + " ASC" + ", " + TaskContract.TaskColumns.COMPLETED + " DESC"
            } else {
                //TODO: think about to using Java constants inside string expressions
                val columnStatus = TaskContract.TaskColumns.STATUS
                val columnStatusDefault = TaskContract.TaskColumns.STATUS_DEFAULT
                val columnStatusCompleted = TaskContract.TaskColumns.STATUS_COMPLETED
                val columnCompleted = TaskContract.TaskColumns.COMPLETED
                selection = "$columnListId = $taskListId AND $columnStatus = $columnStatusDefault OR ($columnStatus == $columnStatusCompleted AND $columnCompleted > $mShowTime)"
                sorting = TaskContract.TaskColumns.CREATED + " ASC"
            }

            return CursorLoader(activity, TaskContract.Tasks.CONTENT_URI, null, selection, null, sorting)
        } else {
            return null
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mAdapter!!.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter!!.swapCursor(null)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        when (id) {
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
                return true
            }
            R.id.show_hidden -> {
                mShowHidden = !item.isChecked
                item.setChecked(mShowHidden!!)
                refreshList()
                return true
            }
            R.id.delete_completed_action -> {
                showConfirmationDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun insertItem() {
        val title = mAddEditText!!.text.toString()
        val isInserted = TaskUtils.Factory.get(activity).insertTask(title, mListId)
        if (isInserted) {
            mAddEditText!!.setText("")
            mAddEditText!!.isFocusable = true
        }
    }

    private fun refreshList() {
        loaderManager.restartLoader(URL_LOADER, activity.intent.extras, this)
        mAdapter!!.notifyDataSetChanged()
    }

    private fun showConfirmationDialog() {
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle(activity.getString(R.string.confirmation))
        alertBuilder.setMessage(activity.getString(R.string.delete_completed_dialog_message))
        alertBuilder.setNegativeButton(android.R.string.no) { dialog, which -> dialog.cancel() }

        alertBuilder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val rows = TaskUtils.Factory.get(activity).deleteCompleted(mListId)
            if (rows > 0) {
                Toast.makeText(activity, R.string.delete_completed_success, Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }
        alertBuilder.create().show()
    }

    companion object {
        private val TAG = "TaskItemsFragment"
        private val URL_LOADER = 0
    }
}
