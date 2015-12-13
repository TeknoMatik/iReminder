package com.rg.ireminders.ui.adapters

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.rg.ireminders.R
import com.rg.ireminders.db.entities.TaskItem
import com.rg.ireminders.db.utils.TaskUtils
import com.rg.ireminders.utils.DateUtils
import java.util.ArrayList
import java.util.TreeMap
import java.util.TreeSet

/**
 * Created by rustamgaifullin on 12/6/15.
 */
class ScheduleListAdapter
/**
 * Constructor
 * @param context activity context
 * *
 * @param taskItemList list should be sorted by a due date, this is very important!
 */
(private val mContext: Context, taskItemList: List<TaskItem>) : BaseAdapter(), CompoundButton.OnCheckedChangeListener {

    private val mData = ArrayList<TaskItem>()
    private val mSectionRows = TreeSet<Int>()

    private val mInflater: LayoutInflater

    init {
        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        buildSections(taskItemList)
    }

    private fun buildSections(taskItemList: List<TaskItem>) {
        if (taskItemList.size == 0) return

        var index = 0
        var lastDaysCount = -1
        while (index < taskItemList.size) {
            val item = taskItemList[index]
            val daysCount = DateUtils.getDaysCount(item.due)
            if (mSectionRows.isEmpty() || lastDaysCount != daysCount) {
                mSectionRows.add(mData.size)
                val separatorItem = TaskItem()
                separatorItem.due = item.due
                separatorItem.title = getSectionName(daysCount)
                mData.add(separatorItem)
                lastDaysCount = daysCount
            } else {
                mData.add(item)
                index++
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mSectionRows.contains(position)) TYPE_SEPARATOR else TYPE_ITEM
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): Any {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        val rowType = getItemViewType(position)
        val item = mData[position]

        if (convertView == null) {
            holder = ViewHolder()
            when (rowType) {
                TYPE_ITEM -> {
                    convertView = mInflater.inflate(R.layout.schedule_list_item, null)
                    holder.tittleTextView = convertView!!.findViewById(R.id.titleTextView) as TextView
                    holder.dueTimeTextView = convertView.findViewById(R.id.dueTextView) as TextView
                    holder.statusCheckBox = convertView.findViewById(R.id.statusCheckBox) as CheckBox
                    holder.statusCheckBox!!.tag = item
                    holder.statusCheckBox!!.setOnCheckedChangeListener(this)
                }
                TYPE_SEPARATOR -> {
                    convertView = mInflater.inflate(R.layout.schedule_list_section, null)
                    if (position == 0) {
                        convertView!!.setPadding(convertView.paddingLeft, 0, convertView.paddingRight, convertView.paddingBottom)
                    }
                    holder.tittleTextView = convertView!!.findViewById(R.id.titleTextView) as TextView
                    holder.dueDateTextView = convertView.findViewById(R.id.dateTextView) as TextView
                }
            }

            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.fill(item)

        return convertView
    }

    private fun getSectionName(daysCount: Int): String {
        val section: String

        when (daysCount) {
            -1 -> section = mContext.getString(R.string.yesterday)
            0 -> section = mContext.getString(R.string.today)
            1 -> section = mContext.getString(R.string.tomorrow)
            else -> if (daysCount > 0) {
                section = "${java.lang.String.format(mContext.getString(R.string.next_days), daysCount)}"
            } else {
                section = "${java.lang.String.format(mContext.getString(R.string.previous_days), -daysCount)}"
            }
        }

        return section
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (buttonView.id == R.id.statusCheckBox) {
            val item = buttonView.tag as TaskItem
            TaskUtils.Factory.get(mContext).changeTaskStatus(item.id, item.listId, isChecked)
        }
    }

    class ViewHolder {
        var statusCheckBox: CheckBox? = null
        var tittleTextView: TextView? = null
        var dueTimeTextView: TextView? = null
        var dueDateTextView: TextView? = null

        fun fill(item: TaskItem) {
            if (statusCheckBox != null) {
                statusCheckBox!!.isChecked = false
            }

            if (tittleTextView != null) {
                tittleTextView!!.text = item.title
            }

            if (dueDateTextView != null) {
                dueDateTextView!!.text = DateUtils.getDueDate(item.due)
            }

            if (dueTimeTextView != null) {
                dueTimeTextView!!.text = DateUtils.getDueTime(item.due)
            }
        }
    }

    companion object {
        private val TYPE_ITEM = 0
        private val TYPE_SEPARATOR = 1
    }
}
