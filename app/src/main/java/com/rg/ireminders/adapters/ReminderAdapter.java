package com.rg.ireminders.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.rg.ireminders.R;
import com.rg.ireminders.db.entities.TaskList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
  private List<TaskList> mDataSet;
  private IReminderClickListener listener;

  public ReminderAdapter(List<TaskList> mDataSet, IReminderClickListener listener) {
    this.mDataSet = mDataSet;
    this.listener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false);

    return new ViewHolder(v, listener);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    TaskList taskList = mDataSet.get(position);

    holder.mTextView.setText(taskList.getName());
    holder.mTextView.setTag(taskList.getId());
    holder.setColor(taskList.getColor());
  }

  @Override
  public int getItemCount() {
    return mDataSet.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mTextView;
    public CardView mCardView;
    public int mColor;
    private IReminderClickListener listener;

    public ViewHolder(View v, IReminderClickListener listener) {
      super(v);
      this.listener = listener;

      mCardView = (CardView) v.findViewById(R.id.card_view);
      mTextView = (TextView) mCardView.findViewById(R.id.info_text);
      mCardView.setOnClickListener(this);
    }

    public void setColor(int color) {
      mTextView.setTextColor(color);
      this.mColor = color;
    }

    @Override
    public void onClick(final View view) {
      listener.onClick(view, mTextView.getText().toString(), (Long) mTextView.getTag(), mColor);
    }
  }

  public static interface IReminderClickListener {
    public void onClick(View v, String item, Long taskId, int color);
  }
}
