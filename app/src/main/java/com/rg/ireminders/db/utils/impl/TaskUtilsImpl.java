package com.rg.ireminders.db.utils.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.rg.ireminders.db.entities.Task;
import com.rg.ireminders.db.entities.TaskList;
import com.rg.ireminders.db.utils.TaskUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.dmfs.provider.tasks.TaskContract;

public class TaskUtilsImpl implements TaskUtils {
  private static final String TAG = "TaskUtilsImpl";
  private ContentResolver mContentResolver;

  public TaskUtilsImpl(ContentResolver contentResolver) {
    this.mContentResolver = contentResolver;
  }

  @Override
  public List<TaskList> getTaskList() {
    List<TaskList> taskList = new ArrayList<>();
    Cursor cursor =
        mContentResolver.query(TaskContract.TaskLists.getContentUri(TaskContract.AUTHORITY), null, null, null, null);
    if (cursor == null) {
      return taskList;
    }

    cursor.moveToFirst();
    while (cursor.moveToNext()) {
      TaskList task = new TaskList();

      task.setId(cursor.getLong(cursor.getColumnIndex(TaskContract.TaskListColumns._ID)));
      task.setName(cursor.getString(cursor.getColumnIndex(TaskContract.TaskListColumns.LIST_NAME)));
      task.setColor(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.LIST_COLOR)));
      task.setOwner(cursor.getString(cursor.getColumnIndex(TaskContract.TaskListColumns.OWNER)));
      task.setSyncEnabled(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.SYNC_ENABLED)));
      task.setVisible(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.VISIBLE)));

      taskList.add(task);
    }

    cursor.close();

    return taskList;
  }

  @Override
  public List<Task> getTaskByTaskListId(Long taskListId) {
    List<Task> tasks = new ArrayList<>();
    String selection = String.format("%s = %s", TaskContract.TaskColumns.LIST_ID, taskListId);
    Cursor cursor = mContentResolver.query(TaskContract.Tasks.CONTENT_URI, null, selection, null, null);
    cursor.moveToFirst();
    while (cursor.moveToNext()) {
      Task task = new Task();

      task.setId(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskColumns._ID)));
      task.setTitle(cursor.getString(cursor.getColumnIndex(TaskContract.TaskColumns.TITLE)));
      task.setCreated(cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.CREATED)));
      task.setDue(cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.DUE)));

      tasks.add(task);
    }
    cursor.close();

    return tasks;
  }

  @Override public boolean insertTask(String taskName, Long taskListId) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(TaskContract.TaskColumns.LIST_ID, taskListId);
    contentValues.put(TaskContract.TaskColumns.TITLE, taskName);

    Uri uri = mContentResolver.insert(TaskContract.Tasks.CONTENT_URI, contentValues);
    return uri != null;
  }

  @Override public void updateTask(Long id, String taskName) {
    updateTask(id, taskName, null);
  }

  @Override public void changeTaskStatus(Long id, boolean completed) {
    updateTask(id, null, completed);
  }

  private void updateTask(Long id, String taskName, Boolean isCompleted) {
    ContentValues contentValues = new ContentValues();
    if (taskName != null) {
      contentValues.put(TaskContract.TaskColumns.TITLE, taskName);
    }
    if (isCompleted != null) {
      int status = isCompleted ? TaskContract.TaskColumns.STATUS_COMPLETED : TaskContract.TaskColumns.STATUS_DEFAULT;
      contentValues.put(TaskContract.TaskColumns.STATUS, status);
    }

    String where = String.format("%s == %d", TaskContract.TaskColumns._ID, id);
    int rows = mContentResolver.update(TaskContract.Tasks.CONTENT_URI, contentValues, where, null);
    Log.d(TAG, "Rows updated: " + rows);
  }
}
