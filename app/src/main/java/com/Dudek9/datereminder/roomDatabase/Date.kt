package com.Dudek9.datereminder.roomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Date_table")
data class Date(var name:String,
                var date:String,
                var countdown:Int,
                var reminder:Int) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var date_id:Int=0

}