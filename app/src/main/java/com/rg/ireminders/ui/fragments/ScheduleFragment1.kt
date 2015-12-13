package com.rg.ireminders.ui.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.rg.ireminders.R
import com.rg.ireminders.db.entities.TaskItem
import com.rg.ireminders.db.utils.TaskUtils
import com.rg.ireminders.ui.adapters.ScheduleListAdapter

/**
 * A placeholder fragment containing a simple view.
 */
class ScheduleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_schedule, container, false)

        val listView = view.findViewById(R.id.listView) as ListView
        val taskItemList = TaskUtils.Factory.get(activity).scheduledTasks
        val adapter = ScheduleListAdapter(activity, taskItemList)
        listView.adapter = adapter

        return view
    }
}
