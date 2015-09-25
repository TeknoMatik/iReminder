package com.rg.ireminders.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.rg.ireminders.R;

public abstract class BaseActivity extends AppCompatActivity {
  protected Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutResource());
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }
  }

  protected abstract int getLayoutResource();
}
