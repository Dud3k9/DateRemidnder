package com.Dudek9.datereminder.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Date::class], version = 1, exportSchema = false)
abstract class DateDatabase : RoomDatabase() {

    abstract fun dateDao(): DateDao

    companion object {
        private var instance: DateDatabase? = null

        fun getInstance(context: Context): DateDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    context,
                    DateDatabase::class.java,
                    "Date_table")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }

        fun deleteInstanceOfDatabase(){
            instance?.close()
            instance=null
        }
    }
}