package com.Dudek9.datereminder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.Dudek9.datereminder.roomDatabase.DateRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity() {


    val dateRepository by lazy { DateRepository(application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButtonOnClick()
    }

    override fun onResume() {
        super.onResume()
        setUpRecyclerView()
    }

    fun setUpRecyclerView(){
        var list= runBlocking {dateRepository.getAllDatesAsync().await()}

        if(list.isEmpty())
            no_dates_text.visibility= View.VISIBLE
        else
            no_dates_text.visibility=View.INVISIBLE
        recycler_view.layoutManager=LinearLayoutManager(this)
        recycler_view.adapter=MyAdapter(this,list)
    }

    fun addButtonOnClick() {
        add_button.setOnClickListener { view ->
            var intent=Intent(this,CreateDateActivity::class.java)
            startActivity(intent)
        }
    }
}