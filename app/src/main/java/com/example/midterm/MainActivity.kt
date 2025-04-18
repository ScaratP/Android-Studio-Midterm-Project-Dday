package com.example.midterm

import EventViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midterm.adapter.MyAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var myAdapter: MyAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val historyFloatingBtn = findViewById<FloatingActionButton>(R.id.historyFloatingBtn)
        val searchFloatingBtn = findViewById<FloatingActionButton>(R.id.searchFloatingBtn)

        myAdapter = MyAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter


        // 觀察 upcomingEvents LiveData 資料，更新 RecyclerView
        lifecycleScope.launch {
            eventViewModel.upcomingEvents.collect { events ->
                myAdapter.submitList(events) // 更新 RecyclerView
            }
        }

        // 新增事件的 FloatingActionButton 按鈕
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java)
            startActivity(intent)
        }

        historyFloatingBtn.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        searchFloatingBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        myAdapter.setOnItemClickListener { event ->
            val intent = Intent(this, EditEventActivity::class.java).apply {
                putExtra("EVENT_DATE", event.date.time) // 傳遞日期的時間戳
            }
            startActivity(intent)
        }
    }
}
