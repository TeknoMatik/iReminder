package com.rg.ireminders.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.entities.TaskItem;
import com.rg.ireminders.db.utils.TaskUtils;
import com.rg.ireminders.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by rustamgaifullin on 12/6/15.
 */
public class ScheduleListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
  private static final int TYPE_ITEM = 0;
  private static final int TYPE_SEPARATOR = 1;

  private List<TaskItem> mData = new ArrayList<>();
  private Set<Integer> mSectionRows = new TreeSet<>();

  private LayoutInflater mInflater;
  private Context mContext;

  /**
   * Constructor
   * @param context activity context
   * @param taskItemList list should be sorted by a due date, this is very important!
   */
  public ScheduleListAdapter(Context context, List<TaskItem> taskItemList) {
    mContext = context;
    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    buildSections(taskItemList);
  }

  private void buildSections(List<TaskItem> taskItemList) {
    if (taskItemList.size() == 0) return;

    int index = 0;
    int lastDaysCount = -1;
    while (index < taskItemList.size()) {
      TaskItem item = taskItemList.get(index);
      int daysCount = DateUtils.getDaysCount(item.getDue());
      if (mSectionRows.isEmpty() || lastDaysCount != daysCount) {
        mSectionRows.add(mData.size());
        TaskItem separatorItem = new TaskItem();
        separatorItem.setDue(item.getDue());
        separatorItem.setTitle(getSectionName(daysCount));
        mData.add(separatorItem);
        lastDaysCount = daysCount;
      } else {
        mData.add(item);
        index++;
      }
    }
  }

  @Override
  public int getItemViewType(int position) {
    return mSectionRows.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override public int getCount() {
    return mData.size();
  }

  @Override public Object getItem(int position) {
    return mData.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    int rowType = getItemViewType(position);
    TaskItem item = mData.get(position);

    if (convertView == null) {
      holder = new ViewHolder();
      switch (rowType) {
        case TYPE_ITEM:
          convertView = mInflater.inflate(R.layout.schedule_list_item, null);
          holder.tittleTextView= (TextView) convertView.findViewById(R.id.titleTextView);
          holder.dueTimeTextView = (TextView) convertView.findViewById(R.id.dueTextView);
          holder.statusCheckBox = (CheckBox) convertView.findViewById(R.id.statusCheckBox);
          holder.statusCheckBox.setTag(item);
          holder.statusCheckBox.setOnCheckedChangeListener(this);
          break;
        case TYPE_SEPARATOR:
          convertView = mInflater.inflate(R.layout.schedule_list_section, null);
          if (position == 0) {
            convertView.setPadding(convertView.getPaddingLeft(), 0, convertView.getPaddingRight(), convertView.getPaddingBottom());
          }
          holder.tittleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
          holder.dueDateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
          break;
      }

      assert convertView != null;
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.fill(item);

    return convertView;
  }

  private String getSectionName(int daysCount) {
    String section;

    switch (daysCount) {
      case -1:
        section = "Yesterday";
        break;
      case 0:
        section = mContext.getString(R.string.today);
        break;
      case 1:
        section = mContext.getString(R.string.tomorrow);
        break;
      default:
        if (daysCount > 0) {
          section = String.format(mContext.getString(R.string.next_days), daysCount);
        } else {
          section = String.format("%d days ago", -daysCount);
        }
    }

    return section;
  }

  @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (buttonView.getId() == R.id.statusCheckBox) {
      TaskItem item = (TaskItem) buttonView.getTag();
      TaskUtils.Factory.get(mContext).changeTaskStatus(item.getId(), item.getListId(), isChecked);
    }
  }

  public static class ViewHolder {
    public CheckBox statusCheckBox;
    public TextView tittleTextView;
    public TextView dueTimeTextView;
    public TextView dueDateTextView;

    public void fill(TaskItem item) {
      if (statusCheckBox != null) {
        statusCheckBox.setChecked(false);
      }

      if (tittleTextView != null) {
        tittleTextView.setText(item.getTitle());
      }

      if (dueDateTextView != null) {
        dueDateTextView.setText(DateUtils.getDueDate(item.getDue()));
      }

      if (dueTimeTextView != null) {
        dueTimeTextView.setText(DateUtils.getDueTime(item.getDue()));
      }
    }
  }
}
