package com.example.midterm

import EventViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midterm.adapter.MyAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class HistoryActivity: AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        val backButton = findViewById<FloatingActionButton>(R.id.floatingActionButton2)

        recyclerView = findViewById(R.id.recyclerView)
        myAdapter = MyAdapter()
        recyclerView.adapter = myAdapter

        // 設定 LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            eventViewModel.allEvents.collect { events ->
                myAdapter.submitList(events)
            }
        }

        myAdapter.setOnItemClickListener { event ->
            val intent = Intent(this, EditEventActivity::class.java).apply {
                putExtra("EVENT_DATE", event.date.time) // 傳遞日期的時間戳
            }
            startActivity(intent)
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}
