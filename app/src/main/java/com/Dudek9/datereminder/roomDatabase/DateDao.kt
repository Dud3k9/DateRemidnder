package com.Dudek9.datereminder.roomDatabase

import androidx.room.*

@Dao
interface DateDao {

    @Insert
    fun insert(date:Date)

    @Update
    fun update(date:Date)

    @Delete
    fun delete(date:Date)

    @Query("SELECT * FROM Date_table")
    fun getAllDates():List<Date>

    @Query("SELECT * FROM Date_table WHERE Date_table.date_id=:id")
    fun getDate(id:Int):List<Date>

    @Query("DELETE FROM Date_table")
    fun deleteAll()
}