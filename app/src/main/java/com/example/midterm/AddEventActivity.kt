package com.example.midterm

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.midterm.data.database.AppDatabase
import com.example.midterm.entity.Event
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AddEventActivity : AppCompatActivity() {

    private lateinit var dateButton: Button
    private lateinit var eventNameEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        dateButton = findViewById(R.id.dateButton)
        eventNameEditText = findViewById(R.id.eventNameEditText)
        colorEditText = findViewById(R.id.colorEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        // 日期選擇器
        dateButton.setOnClickListener {
            val year = selectedDate.get(Calendar.YEAR)
            val month = selectedDate.get(Calendar.MONTH)
            val day = selectedDate.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                dateButton.text = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
            }, year, month, day).show()
        }

        // 儲存按鈕點擊事件
        saveButton.setOnClickListener {
            saveEventToDatabase()
            finish() // 返回到主畫面
        }

        // 取消按鈕點擊事件
        cancelButton.setOnClickListener {
            finish() // 返回到主畫面
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveEventToDatabase() {
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
    }
}
