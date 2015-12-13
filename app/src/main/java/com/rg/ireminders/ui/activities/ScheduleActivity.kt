package com.rg.ireminders.ui.activities

import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import android.view.MenuItem
import android.widget.BaseAdapter
import com.rg.ireminders.R

class ScheduleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    protected override val layoutResource: Int
        get() = R.layout.activity_schedule

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
