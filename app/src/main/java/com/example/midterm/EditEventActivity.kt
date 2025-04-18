package com.example.midterm

import EventViewModel
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.midterm.data.database.AppDatabase
import com.example.midterm.entity.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class EditEventActivity : AppCompatActivity() {
    private lateinit var viewModel: EventViewModel
    private var eventDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        viewModel = ViewModelProvider(this)[EventViewModel::class.java]

        // 使用 getLongExtra 接收時間戳
        val timestamp = intent.getLongExtra("EVENT_DATE", -1L)
        // 將時間戳轉換為 Date
        eventDate = Date(timestamp)



        val dateButton = findViewById<Button>(R.id.dateButton)
        val eventNameEditText = findViewById<EditText>(R.id.eventNameEditText)
        val colorEditText = findViewById<EditText>(R.id.colorEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val delButton = findViewById<Button>(R.id.delButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        lifecycleScope.launch {
            viewModel.getEventByDate(eventDate!!).collect { events ->
                val event = events.firstOrNull()  // 取出第一個事件
                if (event != null) {
                    // 更新 UI 以顯示紀念日資料
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    dateButton.text = dateFormat.format(event.date)
                    eventNameEditText.setText(event.name)
                    colorEditText.setText(event.color)
                    descriptionEditText.setText(event.description)
                } else {
                    // 處理沒有事件的情況
                    finish()
                    return@collect
                }
            }
        }


        // 日期選擇器
        val selectedDate = Calendar.getInstance()
        dateButton.setOnClickListener {
            val year = selectedDate.get(Calendar.YEAR)
            val month = selectedDate.get(Calendar.MONTH)
            val day = selectedDate.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                dateButton.text = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
            }, year, month, day).show()
        }


        // 設置儲存按鈕點擊事件
        updateButton.setOnClickListener {
            viewModel.deleteEvent(eventDate!!)
            val eventName = eventNameEditText.text.toString()
            val color = colorEditText.text.toString()
            val description = descriptionEditText.text.toString()

            val event = Event(
                name = eventName,
                date = selectedDate.time,
                color = color,
                description = description
            )

            // 將事件儲存到資料庫
            val db = AppDatabase.getDatabase(applicationContext)
            GlobalScope.launch(Dispatchers.IO) {
                db.eventDao().insertEvent(event)
            }
            finish()
        }

        // 設置刪除按鈕點擊事件
        delButton.setOnClickListener {
            viewModel.deleteEvent(eventDate!!)
            finish()
        }
        // 設置取消按鈕點擊事件
        cancelButton.setOnClickListener {
            finish()
        }
    }
}