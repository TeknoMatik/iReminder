package com.rg.ireminders.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.rg.ireminders.R

abstract class BaseActivity : AppCompatActivity() {
    protected var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
    }

    protected abstract val layoutResource: Int
}
