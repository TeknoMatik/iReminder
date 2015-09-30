package com.rg.ireminders.ui.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import com.rg.ireminders.R;
import com.rg.ireminders.ui.adapters.TaskItemsCursorAdapter;

public class TaskItemsActivity extends BaseActivity {

  public static final String TASK_LIST_ID_ARG = "taskListId";
  public static final String TASK_LIST_DETAILS_ARG = "taskListDetails";
  public static final String TASK_LIST_COLOR_ARG = "taskListColor";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    onCreateLolipop();
  }

  /**
   * This method prevent status bar from blinking
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void onCreateLolipop() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      postponeEnterTransition();
      final ViewTreeObserver observer = getWindow().getDecorView().getViewTreeObserver();
      observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
          if (observer.isAlive()) {
            observer.removeOnPreDrawListener(this);
          }
          startPostponedEnterTransition();
          return true;
        }
      });
    }
  }

  @Override
  protected int getLayoutResource() {
    return R.layout.activity_details;
  }

  public void textClick(View view) {
    supportFinishAfterTransition();
  }
}
