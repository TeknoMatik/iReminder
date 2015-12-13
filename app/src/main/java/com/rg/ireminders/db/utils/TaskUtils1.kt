package com.rg.ireminders.db.utils

import android.content.Context
import com.rg.ireminders.db.entities.TaskItem
import com.rg.ireminders.db.entities.TaskList
import com.rg.ireminders.db.utils.impl.TaskUtilsImpl

/**
 * Created by rustamgaifullin on 2/7/15.
 */
interface TaskUtils {
    val taskList: List<TaskList>

    fun getTaskByTaskListId(taskListId: Long?): List<TaskItem>

    fun insertTask(taskName: String, taskListId: Long?): Boolean

    fun updateTask(id: Long?, listId: Long?, taskName: String)

    fun changeTaskStatus(id: Long?, listId: Long?, completed: Boolean)

    fun addReminder(id: Long?, listId: Long?, dueDate: Long?)

    fun removeReminder(id: Long?, listId: Long?)

    fun deleteCompleted(listId: Long?): Int

    val scheduledTasks: List<TaskItem>

    class Factory {

        protected fun newForContext(context: Context): TaskUtilsImpl {
            return TaskUtilsImpl(context.contentResolver)
        }

        companion object {
            var instance = Factory()
                private set

            operator fun get(context: Context): TaskUtilsImpl {
                return instance.newForContext(context)
            }

            fun overrideInstance(factory: Factory) {
                instance = factory
            }
        }
    }
}
