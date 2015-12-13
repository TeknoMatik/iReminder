package com.rg.ireminders.db.utils.impl

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.rg.ireminders.db.entities.TaskItem
import com.rg.ireminders.db.entities.TaskList
import com.rg.ireminders.db.utils.TaskUtils
import java.util.ArrayList
import java.util.TimeZone
import org.dmfs.provider.tasks.TaskContract

class TaskUtilsImpl(private val mContentResolver: ContentResolver) : TaskUtils {

    override val taskList: List<TaskList>
        get() {
            val taskList = ArrayList<TaskList>()
            val cursor = mContentResolver.query(TaskContract.TaskLists.getContentUri(TaskContract.AUTHORITY), null, null, null, null) ?: return taskList

            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val task = TaskList()

                task.id = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskListColumns._ID))
                task.name = cursor.getString(cursor.getColumnIndex(TaskContract.TaskListColumns.LIST_NAME))
                task.color = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.LIST_COLOR))
                task.owner = cursor.getString(cursor.getColumnIndex(TaskContract.TaskListColumns.OWNER))
                task.syncEnabled = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.SYNC_ENABLED))
                task.visible = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.VISIBLE))

                taskList.add(task)
            }

            cursor.close()

            return taskList
        }

    override fun getTaskByTaskListId(taskListId: Long?): List<TaskItem> {
        val tasks = ArrayList<TaskItem>()
        val selection = TaskContract.TaskColumns.LIST_ID + " = $taskListId"
        val cursor = mContentResolver.query(TaskContract.Tasks.CONTENT_URI, null, selection, null, null)
        cursor.moveToFirst()
        while (cursor.moveToNext()) {
            val task = TaskItem()

            task.id = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns._ID))
            task.title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskColumns.TITLE))
            task.created = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.CREATED))
            task.due = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.DUE))

            tasks.add(task)
        }
        cursor.close()

        return tasks
    }

    override fun insertTask(taskName: String, taskListId: Long?): Boolean {
        val contentValues = ContentValues()
        contentValues.put(TaskContract.TaskColumns.LIST_ID, taskListId)
        contentValues.put(TaskContract.TaskColumns.TITLE, taskName)

        val uri = mContentResolver.insert(TaskContract.Tasks.CONTENT_URI, contentValues)
        return uri != null
    }

    override fun updateTask(id: Long?, listId: Long?, taskName: String) {
        updateTask(id, listId, null, taskName, null)
    }

    override fun changeTaskStatus(id: Long?, listId: Long?, completed: Boolean) {
        updateTask(id, listId, null, null, completed)
    }

    override fun addReminder(id: Long?, listId: Long?, dueDate: Long?) {
        updateTask(id, listId, dueDate, null, null)
    }

    override fun removeReminder(id: Long?, listId: Long?) {
        updateTask(id, listId, 0L, null, null)
    }

    private fun updateTask(id: Long?, listId: Long?, dueDate: Long?, taskName: String?, isCompleted: Boolean?) {
        val contentValues = ContentValues()
        if (taskName != null) {
            contentValues.put(TaskContract.TaskColumns.TITLE, taskName)
        }
        if (isCompleted != null) {
            val status = if (isCompleted) TaskContract.TaskColumns.STATUS_COMPLETED else TaskContract.TaskColumns.STATUS_DEFAULT
            contentValues.put(TaskContract.TaskColumns.STATUS, status)
        }
        if (dueDate != null) {
            if (dueDate === 0L) {
                var nullValue: Long? = null
                contentValues.put(TaskContract.TaskColumns.DUE, nullValue)
                contentValues.put(TaskContract.TaskColumns.TZ, "")
            } else {
                contentValues.put(TaskContract.TaskColumns.DUE, dueDate)
                contentValues.put(TaskContract.TaskColumns.TZ, TimeZone.getDefault().id)
            }
        }

        val taskIdWhere = TaskContract.TaskColumns._ID + " == $id"
        val listIdWhere = TaskContract.TaskColumns.LIST_ID + " == $listId"
        val rows = mContentResolver.update(TaskContract.Tasks.CONTENT_URI, contentValues,
                taskIdWhere + " AND " + listIdWhere, null)
        Log.d(TAG, "Rows updated: " + rows)
    }

    override fun deleteCompleted(listId: Long?): Int {
        val where = TaskContract.TaskColumns.LIST_ID + " = " + listId + " AND " + TaskContract.TaskColumns.STATUS + " = " + TaskContract.TaskColumns.STATUS_COMPLETED
        val rows = mContentResolver.delete(TaskContract.Tasks.CONTENT_URI, where, null)
        Log.d(TAG, "Rows deleted: " + rows)
        return rows
    }

    override val scheduledTasks: List<TaskItem>
        get() {
            val selection = TaskContract.TaskColumns.STATUS + " = " + TaskContract.TaskColumns.STATUS_DEFAULT + " AND "+ TaskContract.TaskColumns.DUE + " > " + 0
            val sorting = TaskContract.TaskColumns.DUE + " ASC"
            val cursor = mContentResolver.query(TaskContract.Tasks.CONTENT_URI, null, selection, null, sorting)

            val taskItemList = ArrayList<TaskItem>()
            while (cursor.moveToNext()) {
                val task = TaskItem()
                task.fromCursor(cursor)
                taskItemList.add(task)
            }
            cursor.close()

            return taskItemList
        }

    companion object {
        private val TAG = "TaskUtilsImpl"
    }
}
