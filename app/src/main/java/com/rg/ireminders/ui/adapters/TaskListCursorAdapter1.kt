package com.rg.ireminders.ui.adapters

import android.content.Context
import android.database.Cursor
import android.view.View
import android.widget.ResourceCursorAdapter
import android.widget.TextView
import com.rg.ireminders.R
import com.rg.ireminders.db.entities.TaskList

/**
 * Created by rustamgaifullin on 6/5/15.
 */
class TaskListCursorAdapter(context: Context, layout: Int, c: Cursor?, flags: Int) : ResourceCursorAdapter(context, layout, c, flags) {

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val taskList = TaskList()
        taskList.fromCursor(cursor)
        val textView = view.findViewById(R.id.info_text) as TextView
        textView.text = taskList.name
        textView.setTextColor(taskList.color!!)
    }
}
