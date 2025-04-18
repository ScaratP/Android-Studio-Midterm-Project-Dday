package com.example.midterm.data.dao

import androidx.room.*
import com.example.midterm.entity.Event
import kotlinx.coroutines.flow.Flow
import java.util.Date


@Dao
interface EventDao {

    // 插入或替換紀念日事件
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    // 更新紀念日事件
    @Update
    suspend fun updateEvent(event: Event)

    // 刪除紀念日事件（根據 Event 對象進行刪除）
    @Delete
    suspend fun deleteEvent(event: Event)

    // 獲取所有事件，按日期升序排列
    @Query("SELECT * FROM events ORDER BY date ASC")
    fun getAllEvents(): Flow<List<Event>>

    // 獲取即將到來的事件（日期為當前日期之後），按日期升序排列
    @Query("SELECT * FROM events WHERE date >= :currentDate ORDER BY date ASC")
    fun getUpcomingEvents(currentDate: Long = System.currentTimeMillis()): Flow<List<Event>>

    // 獲取在指定日期範圍內的事件（例如一週內的事件）
    @Query("SELECT * FROM events WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getEventsInDateRange(startDate: Long, endDate: Long): Flow<List<Event>>

    // 計算即將到來的事件數量（可用於顯示有多少事件在未來）
    @Query("SELECT COUNT(*) FROM events WHERE date >= :currentDate")
    fun countUpcomingEvents(currentDate: Long = System.currentTimeMillis()): Flow<Int>

    // 刪除所有過去的事件（例如當紀念日已經過期）
    @Query("DELETE FROM events WHERE date < :currentDate")
    suspend fun deletePastEvents(currentDate: Long = System.currentTimeMillis())

    // 刪除所有紀念日（可用於清空資料庫）
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()

    // 搜索事件名稱包含特定關鍵字的事件
    @Query("SELECT * FROM events WHERE name LIKE '%' || :query || '%' ORDER BY date ASC")
    fun searchEventsByName(query: String): Flow<List<Event>>

    // 根據日期查詢紀念日事件
    @Query("SELECT * FROM events WHERE date = :date")
    fun getEventsByDate(date: Date): Flow<List<Event>>

    // 根據日期範圍查詢紀念日事件
    @Query("SELECT * FROM events WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getEventsByDateRange(startDate: Long, endDate: Long): Flow<List<Event>>


    // 查詢今日紀念日事件
    @Query("SELECT * FROM events WHERE date = :today")
    fun getEventsForToday(today: Long): Flow<List<Event>>

    // 刪除特定日期的紀念日事件
    @Query("DELETE FROM events WHERE date = :date")
    fun deleteEventByDate(date: Date): Int
}
