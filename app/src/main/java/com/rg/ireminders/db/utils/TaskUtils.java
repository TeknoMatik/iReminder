package com.rg.ireminders.db.utils;

import android.content.Context;
import com.rg.ireminders.db.entities.Task;
import com.rg.ireminders.db.entities.TaskList;
import com.rg.ireminders.db.utils.impl.TaskUtilsImpl;
import java.util.List;

/**
 * Created by rustamgaifullin on 2/7/15.
 */
public interface TaskUtils {
  public List<TaskList> getTaskList();

  public List<Task> getTaskByTaskListId(Long taskListId);

  public boolean insertTask(String taskName, Long taskListId);

  public void updateTask(Long id, String taskName);

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
