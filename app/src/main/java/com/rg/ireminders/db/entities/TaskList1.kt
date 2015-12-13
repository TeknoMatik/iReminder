package com.rg.ireminders.db.entities

import android.database.Cursor
import org.dmfs.provider.tasks.TaskContract

class TaskList {
    var id: Long? = null
    var name: String? = null
    var color: Int? = null
    var visible: Int? = null
    var syncEnabled: Int? = null
    var owner: String? = null

    fun fromCursor(cursor: Cursor) {
        id = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskListColumns._ID))
        name = cursor.getString(cursor.getColumnIndex(TaskContract.TaskListColumns.LIST_NAME))
        color = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.LIST_COLOR))
        owner = cursor.getString(cursor.getColumnIndex(TaskContract.TaskListColumns.OWNER))
        syncEnabled = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.SYNC_ENABLED))
        visible = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.VISIBLE))
    }

    override fun toString(): String {
        return "TaskList{id=$id, name='$name', color=$color, visible=$visible, syncEnabled=$syncEnabled, owner='$owner'}"
    }
}
