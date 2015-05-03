package com.rg.ireminders.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import com.rg.ireminders.R;
import java.util.List;

/**
 * Created by rustamgaifullin on 4/12/15.
 */
public class ArrayTaskListAdapter extends BaseAdapter {

  private List<String> mItemList;
  private Context mContext;

  public ArrayTaskListAdapter(Context context, List<String> itemList) {
    this.mContext = context;
    this.mItemList = itemList;
  }

  @Override public int getCount() {
    return mItemList.size();
  }

  @Override public Object getItem(int position) {
    return mItemList.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    String title = mItemList.get(position);

    LinearLayout layout;
    if (convertView == null) {
      LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      layout = (LinearLayout) mInflater.inflate(R.layout.details_item, null);
    } else {
      layout = (LinearLayout) convertView;
    }

    RadioButton rb = (RadioButton) layout.findViewById(R.id.radioButton);
    rb.setText(title);
    rb.setChecked(false);
    return layout;
  }
}
