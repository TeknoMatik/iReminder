package com.rg.ireminders.db.entities;

import android.database.Cursor;
import org.dmfs.provider.tasks.TaskContract;

public class TaskList {
  private Long id;
  private String name;
  private Integer color;
  private Integer visible;
  private Integer syncEnabled;
  private String owner;

  public void fromCursor(Cursor cursor) {
    setId(cursor.getLong(cursor.getColumnIndex(TaskContract.TaskListColumns._ID)));
    setName(cursor.getString(cursor.getColumnIndex(TaskContract.TaskListColumns.LIST_NAME)));
    setColor(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.LIST_COLOR)));
    setOwner(cursor.getString(cursor.getColumnIndex(TaskContract.TaskListColumns.OWNER)));
    setSyncEnabled(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.SYNC_ENABLED)));
    setVisible(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskListColumns.VISIBLE)));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getColor() {
    return color;
  }

  public void setColor(Integer color) {
    this.color = color;
  }

  public Integer getVisible() {
    return visible;
  }

  public void setVisible(Integer visible) {
    this.visible = visible;
  }

  public Integer getSyncEnabled() {
    return syncEnabled;
  }

  public void setSyncEnabled(Integer syncEnabled) {
    this.syncEnabled = syncEnabled;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public String toString() {
    return "TaskList{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", color=" + color +
        ", visible=" + visible +
        ", syncEnabled=" + syncEnabled +
        ", owner='" + owner + '\'' +
        '}';
  }
}
