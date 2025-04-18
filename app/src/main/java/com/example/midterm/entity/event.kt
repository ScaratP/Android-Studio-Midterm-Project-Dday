package com.example.midterm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val date: Date,
    val color: String ?= null, // 可以使用字符串來保存顏色代碼
    val description: String,
)
