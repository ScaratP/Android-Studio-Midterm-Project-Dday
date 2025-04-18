package com.example.midterm.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.midterm.R
import com.example.midterm.entity.Event
import java.text.SimpleDateFormat
import java.util.*

class MyAdapter : ListAdapter<Event, MyAdapter.MyViewHolder>(EventDiffCallback()) {


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)          // 日期顯示
        val daysLeftTextView: TextView = itemView.findViewById(R.id.daysLeftTextView)  // 距離紀念日的天數顯示
        val container: View = itemView.findViewById(R.id.cardContainer)               // 卡片背景容器
    }

    private var onItemClickListener: ((Event) -> Unit)? = null

    fun setOnItemClickListener(listener: (Event) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }

        // 設定標題和描述
        holder.titleTextView.text = item.name
        holder.descriptionTextView.text = item.description

        // 格式化日期顯示
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        holder.dateTextView.text = dateFormat.format(item.date)

        // 計算距離紀念日的天數
        val today = Calendar.getInstance().apply {
            // 設定為當天的起始時間，避免計算出部分天數的誤差
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        // 計算兩個日期之間的天數差異
        val daysDifference = ((item.date.time - today.time) / (1000 * 60 * 60 * 24)).toInt()

        when {
            daysDifference == 0 -> {
                holder.daysLeftTextView.text = "今天就是"
            }
            daysDifference < 0 -> {
                holder.daysLeftTextView.text = "過了 ${-daysDifference} 天"
            }
            else -> {
                holder.daysLeftTextView.text = "還有 $daysDifference 天"
            }
        }

        val colorString = item.color

        // 更新卡片背景顏色
        try {
            val color = if (colorString.isNullOrEmpty()) {
                // 提供一個預設顏色，例如白色
                Color.parseColor("#FFFFFF") // 默認顏色
            } else {
                Color.parseColor(colorString) // 解析顏色
            }
            holder.itemView.setBackgroundColor(color)
        } catch (e: IllegalArgumentException) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF")) // 默認顏色
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}
