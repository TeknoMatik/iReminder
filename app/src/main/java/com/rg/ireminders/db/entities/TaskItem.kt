package com.rg.ireminders.db.entities

import android.database.Cursor
import org.dmfs.provider.tasks.TaskContract

/**
 * Created by rustamgaifullin on 2/7/15.
 */
class TaskItem {
    var id: Long? = null
    var title: String? = null
    var status: Int? = null
    var created: Long? = null
    var due: Long? = null
    var listId: Long? = null

    fun fromCursor(cursor: Cursor) {
        title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskColumns.TITLE))
        status = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskColumns.STATUS))
        due = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.DUE))
        id = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns._ID))
        listId = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.LIST_ID))
    }

    override fun toString(): String {
        return "TaskItem{id=$id, title='$title', status=$status, created=$created, due=$due, listId=$listId}"
    }
}
