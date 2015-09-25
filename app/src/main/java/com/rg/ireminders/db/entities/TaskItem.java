package com.rg.ireminders.db.entities;

import android.database.Cursor;
import org.dmfs.provider.tasks.TaskContract;

/**
 * Created by rustamgaifullin on 2/7/15.
 */
public class TaskItem {
  private Long id;
  private String title;
  private Integer status;
  private Long created;
  private Long due;

  public void fromCursor(Cursor cursor) {
    title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskColumns.TITLE));
    status = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskColumns.STATUS));
    due = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns.DUE));
    id = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskColumns._ID));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

  public Long getDue() {
    return due;
  }

  public void setDue(Long due) {
    this.due = due;
  }

  @Override public String toString() {
    return "TaskItem{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", status=" + status +
        ", created=" + created +
        ", due=" + due +
        '}';
  }
}
