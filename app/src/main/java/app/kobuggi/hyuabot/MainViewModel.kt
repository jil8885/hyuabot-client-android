package app.kobuggi.hyuabot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import app.kobuggi.hyuabot.service.database.AppDatabase
import app.kobuggi.hyuabot.service.database.AppDatabaseDao
import app.kobuggi.hyuabot.service.database.AppDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appDatabaseRepository: AppDatabaseRepository) : ViewModel() {
    fun upgradeDatabase (assetsPath: String){
        // 데이터베이스 업데이트
        val database = Room.databaseBuilder(GlobalApplication.getApplicationContext(), AppDatabase::class.java, assetsPath)
            .fallbackToDestructiveMigration()
            .build()
        val dao = database.databaseDao()
        viewModelScope.launch {
            appDatabaseRepository.getAll().collect{ data ->
                dao.getAll().collect{ asset ->
                    if (data.isEmpty() || data != asset){
                        initializeAppDatabase(dao)
                    }
                }
            }
        }
        viewModelScope.launch {
            appDatabaseRepository.getAllEvents().collect{
                dao.getAllEvents().collect{ asset ->
                    if (it.isEmpty() || it != asset){
                        initializeCalendarDatabase(dao)
                    }
                }
            }
        }
    }

    fun initializeDatabase (assetsPath: String) {
        // 데이터베이스 업데이트
        val database = Room.databaseBuilder(GlobalApplication.getApplicationContext(), AppDatabase::class.java, assetsPath)
            .fallbackToDestructiveMigration()
            .build()
        val dao = database.databaseDao()
        viewModelScope.launch {
            appDatabaseRepository.deleteAll()
            dao.getAll().collect{
                appDatabaseRepository.insertAll(it)
            }
            appDatabaseRepository.deleteAllEvents()
            dao.getAllEvents().collect{
                appDatabaseRepository.insertAllEvents(it)
            }
        }
    }

    private fun initializeAppDatabase(dao: AppDatabaseDao){
        viewModelScope.launch {
            appDatabaseRepository.deleteAll()
            dao.getAll().collect {
                appDatabaseRepository.insertAll(it)
            }
        }
    }

    private fun initializeCalendarDatabase(dao: AppDatabaseDao){
        viewModelScope.launch {
            appDatabaseRepository.deleteAllEvents()
            dao.getAllEvents().collect {
                appDatabaseRepository.insertAllEvents(it)
            }
        }
    }
}