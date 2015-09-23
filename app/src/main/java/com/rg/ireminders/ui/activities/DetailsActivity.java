package com.rg.ireminders.ui.activities;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.ui.adapters.DetailsCursorAdapter;
import com.rg.ireminders.db.utils.TaskUtils;
import java.util.Date;
import org.dmfs.provider.tasks.TaskContract;

public class DetailsActivity extends BaseActivity {

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
