package com.rg.ireminders.ui.activities

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import com.rg.ireminders.R
import com.rg.ireminders.ui.fragments.TaskListFragment
import java.util.ArrayList

class TaskListActivity : BaseActivity(), TaskListFragment.OnTaskListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected override val layoutResource: Int
        get() = R.layout.activity_list

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.open_schedule -> {
                val intent = Intent(this, ScheduleActivity::class.java)
                startActivity(intent)

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onTaskListItemClick(view: View, taskName: String, taskId: Long?, taskColor: Int?) {
        val intent = Intent(this@TaskListActivity, TaskItemsActivity::class.java)
        intent.putExtra(TaskItemsActivity.TASK_LIST_DETAILS_ARG, taskName)
        intent.putExtra(TaskItemsActivity.TASK_LIST_ID_ARG, taskId)
        intent.putExtra(TaskItemsActivity.TASK_LIST_COLOR_ARG, taskColor)
        val transitionView = view.findViewById(R.id.transition_view)
        val pairs = createPairs(transitionView)
        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this@TaskListActivity,
                *pairs.toTypedArray())
        ActivityCompat.startActivity(this@TaskListActivity, intent, activityOptions.toBundle())
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createPairs(v: View?): MutableList<Pair<View, String>> {
        val pairs :MutableList<Pair<View, String>> = arrayListOf();
        val navigationBarBackground = findViewById(android.R.id.navigationBarBackground)
        if (navigationBarBackground != null) {
            pairs.add(Pair.create(navigationBarBackground, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME))
        }

        val statusBarBackground = findViewById(android.R.id.statusBarBackground)
        if (statusBarBackground != null) {
            pairs.add(Pair.create(statusBarBackground, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME))
        }

        if (toolbar != null) {
            pairs.add(Pair.create<View, String>(toolbar as View?, "toolbar"))
        }
        if (v != null) {
            pairs.add(Pair.create(v, "item"))
        }

        return pairs
    }
}
