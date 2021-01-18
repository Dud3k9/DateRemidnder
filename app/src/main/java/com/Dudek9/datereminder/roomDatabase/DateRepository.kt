package com.Dudek9.datereminder.roomDatabase

import android.app.Application
import android.app.Person
import androidx.room.CoroutinesRoom
import kotlinx.coroutines.*

class DateRepository(applicaion:Application) {

    private lateinit var dateDao:DateDao

    init{
        val database=DateDatabase
            .getInstance(applicaion.applicationContext)

        dateDao=database!!.dateDao()
    }

    fun insertDate(date:Date){
        CoroutineScope(Dispatchers.IO).launch {
            dateDao.insert(date)
        }
    }

    fun deleteDate(date:Date){
        CoroutineScope(Dispatchers.IO).launch {
            dateDao.delete(date)
        }
    }

    fun updateDate(date:Date){
        CoroutineScope(Dispatchers.IO).launch {
            dateDao.update(date)
        }
    }

    fun deleteAllDates(){
        CoroutineScope(Dispatchers.IO).launch {
            dateDao.deleteAll()
        }
    }

    fun getAllDatesAsync():Deferred<List<Date>>{
        return CoroutineScope(Dispatchers.IO).async {
            dateDao.getAllDates()
        }
    }

    fun getDateAsync(id:Int): Deferred<List<Date>> {
        return CoroutineScope(Dispatchers.IO).async {
            dateDao.getDate(id)
        }
    }

}