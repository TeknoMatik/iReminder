package com.rg.ireminders.db.utils;

import android.content.Context;
import com.rg.ireminders.db.entities.TaskItem;
import com.rg.ireminders.db.entities.TaskList;
import com.rg.ireminders.db.utils.impl.TaskUtilsImpl;
import java.util.List;

/**
 * Created by rustamgaifullin on 2/7/15.
 */
public interface TaskUtils {
  List<TaskList> getTaskList();

  List<TaskItem> getTaskByTaskListId(Long taskListId);

  boolean insertTask(String taskName, Long taskListId);

  void updateTask(Long id, Long listId, String taskName);

  void changeTaskStatus(Long id, Long listId, boolean completed);

  void addReminder(Long id, Long listId, Long dueDate);

  void removeReminder(Long id, Long listId);

  int deleteCompleted(Long listId);

  public static class Factory {
    private static Factory instance = new Factory();

    public static TaskUtilsImpl get(Context context) {
      return instance.newForContext(context);
    }

    public static Factory getInstance() {
      return instance;
    }

    public static void overrideInstance(Factory factory) {
      instance = factory;
    }

    protected TaskUtilsImpl newForContext(Context context) {
      return new TaskUtilsImpl(context.getContentResolver());
    }
  }
}
