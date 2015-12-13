package com.rg.ireminders.ui.activities

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.rg.ireminders.R

class TaskItemsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateLolipop()
    }

    /**
     * This method prevent status bar from blinking
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onCreateLolipop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            val observer = window.decorView.viewTreeObserver
            observer.addOnPreDrawListener {
                if (observer.isAlive) {
//                    observer.removeOnPreDrawListener(this)
                }
                startPostponedEnterTransition()
                true
            }
        }
    }

    protected override val layoutResource: Int
        get() = R.layout.activity_details

    fun textClick(view: View) {
        supportFinishAfterTransition()
    }

    companion object {

        val TASK_LIST_ID_ARG = "taskListId"
        val TASK_LIST_DETAILS_ARG = "taskListDetails"
        val TASK_LIST_COLOR_ARG = "taskListColor"
    }
}
