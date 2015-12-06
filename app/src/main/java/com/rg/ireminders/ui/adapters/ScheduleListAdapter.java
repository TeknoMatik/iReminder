package com.rg.ireminders.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.entities.TaskItem;
import com.rg.ireminders.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by rustamgaifullin on 12/6/15.
 */
public class ScheduleListAdapter extends BaseAdapter {
  private static final int TYPE_ITEM = 0;
  private static final int TYPE_SEPARATOR = 1;

  private List<TaskItem> mData = new ArrayList<>();
  private Map<Integer, Integer> mSectionHeader = new TreeMap<>();

  private LayoutInflater mInflater;

  /**
   * Constructor
   * @param context activity context
   * @param taskItemList list should be sorted by a due date, this is very important!
   */
  public ScheduleListAdapter(Context context, List<TaskItem> taskItemList) {
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
      if (mSectionHeader.isEmpty() || lastDaysCount != daysCount) {
        mSectionHeader.put(mData.size(), daysCount);
        mData.add(null);
        lastDaysCount = daysCount;
      } else {
        mData.add(item);
        index++;
      }
    }
  }

  @Override
  public int getItemViewType(int position) {
    return mSectionHeader.keySet().contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
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

    if (convertView == null) {
      holder = new ViewHolder();
      switch (rowType) {
        case TYPE_ITEM:
          convertView = mInflater.inflate(R.layout.schedule_list_item, null);
          holder.tittleTextView= (TextView) convertView.findViewById(R.id.titleTextView);
          holder.statusCheckBox = (CheckBox) convertView.findViewById(R.id.statusCheckBox);
          holder.dateTextView = (TextView) convertView.findViewById(R.id.dueTextView);
          break;
        case TYPE_SEPARATOR:
          convertView = mInflater.inflate(R.layout.schedule_list_section, null);
          holder.tittleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
          break;
      }

      assert convertView != null;
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    if (rowType == TYPE_SEPARATOR) {
      String section = getSectionName(mSectionHeader.get(position));
      holder.fill(section);
    } else {
      holder.fill(mData.get(position));
    }
    return convertView;
  }

  private String getSectionName(int daysCount) {
    String section;
    if (daysCount == 0) {
      section = "Today";
    } else if (daysCount == 1) {
      section = "Tomorrow";
    } else {
      section = String.format("Next %d days", daysCount);
    }

    return section;
  }

  public static class ViewHolder {
    public CheckBox statusCheckBox;
    public TextView tittleTextView;
    public TextView dateTextView;

    public void fill(String title) {
      tittleTextView.setText(title);
    }

    public void fill(TaskItem item) {
      statusCheckBox.setChecked(false);
      tittleTextView.setText(item.getTitle());
      dateTextView.setText(DateUtils.getDueTime(item.getDue()));
    }
  }
}
