package com.rg.ireminders.ui.fragments

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.AdapterView
import android.widget.ListView
import com.rg.ireminders.R
import com.rg.ireminders.db.entities.TaskList
import com.rg.ireminders.ui.adapters.TaskListCursorAdapter
import org.dmfs.provider.tasks.TaskContract

/**
 * A task list fragment [Fragment] subclass.
 */
class TaskListFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private var mAdapter: TaskListCursorAdapter? = null
    private var mListener: OnTaskListFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_task_list, container, false)

        mAdapter = TaskListCursorAdapter(activity, R.layout.list_item, null, 0)
        val listView = view.findViewById(R.id.listView) as ListView
        listView.adapter = mAdapter
        listView.onItemClickListener = this

        val bundle : Bundle = Bundle()
        loaderManager.initLoader(URL_LOADER, bundle, this)

        return view
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor>? {
        if (id == URL_LOADER) {
            val uri = TaskContract.TaskLists.getContentUri(TaskContract.AUTHORITY)
            val selection = "account_type != \"LOCAL\""
            return CursorLoader(activity, uri, null, selection, null, null)
        } else {
            return null
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mAdapter!!.changeCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter!!.changeCursor(null)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val cursor = mAdapter!!.getItem(position) as Cursor
        val taskList = TaskList()
        taskList.fromCursor(cursor)

        if (mListener != null) {
            mListener!!.onTaskListItemClick(view, taskList.name!!, taskList.id, taskList.color)
        }
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            mListener = activity as OnTaskListFragmentInteractionListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement OnTaskListFragmentInteractionListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnTaskListFragmentInteractionListener {
        fun onTaskListItemClick(view: View, taskName: String, taskId: Long?, taskColor: Int?)
    }

    companion object {

        private val URL_LOADER = 0
    }
}// Required empty public constructor
