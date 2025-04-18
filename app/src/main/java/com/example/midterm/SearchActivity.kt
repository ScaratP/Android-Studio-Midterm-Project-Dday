package com.example.midterm

import EventViewModel
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midterm.adapter.MyAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.Calendar

class SearchActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var dateButton: Button
    private var selectedDate: Calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        // 設定返回按鈕的點擊事件
        val backButton = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        val textView = findViewById<TextView>(R.id.textView)
        backButton.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView)
        myAdapter = MyAdapter()
        recyclerView.adapter = myAdapter

        // 設定 LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 設定日期選擇器按鈕的點擊事件
        dateButton = findViewById(R.id.dateButton)
        dateButton.setOnClickListener {
            val year = selectedDate.get(Calendar.YEAR)
            val month = selectedDate.get(Calendar.MONTH)
            val day = selectedDate.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                dateButton.text = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"

                lifecycleScope.launch {
                    eventViewModel.searchEventsByDate(selectedDate.time).collect { events ->
                        if (events.isEmpty()) {
                            textView.text = "無搜尋結果"
                            myAdapter.submitList(emptyList())
                        }
                        else {
                            textView.text = "搜尋結果"
                            myAdapter.submitList(events) // 更新 RecyclerView
                        }
                    }
                }
            }, year, month, day).show()
        }


        /*val date = selectedDate.time

        // 觀察 upcomingEvents LiveData 資料，更新 RecyclerView
        lifecycleScope.launch {
            eventViewModel.searchEventsByDate(date).collect { events ->
                myAdapter.submitList(events) // 更新 RecyclerView
            }
        }*/
        myAdapter.setOnItemClickListener { event ->
            val intent = Intent(this, EditEventActivity::class.java).apply {
                putExtra("EVENT_DATE", event.date.time) // 傳遞日期的時間戳
            }
            startActivity(intent)
        }

    }

}
