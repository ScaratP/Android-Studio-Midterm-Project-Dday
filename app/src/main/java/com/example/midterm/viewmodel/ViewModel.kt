import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.midterm.data.database.AppDatabase
import com.example.midterm.entity.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val eventDao = AppDatabase.getDatabase(application).eventDao()

    val upcomingEvents: Flow<List<Event>> = eventDao.getUpcomingEvents()
    fun getEventByDate(date:Date): Flow<List<Event>> {
        return eventDao.getEventsByDate(date)
    }

    fun deleteEvent(date:Date) {
        viewModelScope.launch(Dispatchers.IO) {
            eventDao.deleteEventByDate(date)
        }
    }

    val allEvents: Flow<List<Event>> = eventDao.getAllEvents()

    // 根據日期搜尋事件
    fun searchEventsByDate(date: Date): Flow<List<Event>> {
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val startOfNextDay = calendar.timeInMillis

        return eventDao.getEventsByDateRange(startOfDay, startOfNextDay - 1)
    }



}