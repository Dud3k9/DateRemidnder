package com.Dudek9.datereminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.Dudek9.datereminder.roomDatabase.Date
import com.Dudek9.datereminder.roomDatabase.DateRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_create_date.*
import kotlinx.coroutines.runBlocking
import java.util.*

class CreateDateActivity : AppCompatActivity() {

    lateinit var onDateSetListener:DatePickerDialog.OnDateSetListener
    val dateRepository by lazy{DateRepository(this.application)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_date)
        supportActionBar?.hide()


        onDataListener()
        date_picker_text.setOnClickListener { showDataPicker() }
        onClickCancelButtonListener()
        onClickAddDateButtonListener()
    }

    private fun onClickAddDateButtonListener() {
        ok_button.setOnClickListener {
            if(checkRequired(it)){
                val name=name_edittext.text.toString()
                val date=date_picker_text.text.toString()
                val countdown=if((countdown_radiogroup.get(0) as RadioButton).isChecked) 0 else 1
                val reminder=checkReminderCheckbox()
                runBlocking { dateRepository.insertDate(Date(name, date, countdown, reminder))
                dateRepository.getAllDatesAsync().await()}
                var listDates= runBlocking { dateRepository.getAllDatesAsync().await() }
                var dbDate= listDates.filter { it.name==name }.firstOrNull()
                if (dbDate != null) {
                    Notification().setReminders(this,dbDate,false)
                }
                finish()
            }
        }
    }

    private fun checkReminderCheckbox(): Int {
        var tmp=0
        if(month_earlier_checkbox.isChecked)
            tmp+=100
        if(week_earlier_checkbox.isChecked)
            tmp+=10
        if(day_earlier_checkbox.isChecked)
            tmp+=1
        return tmp
    }

    private fun checkRequired(view: View): Boolean {
        if(name_edittext.text.toString()=="") {
            Snackbar.make(view, "name is required", Snackbar.LENGTH_LONG).show()
            return false
        }else if(date_picker_text.text.toString()=="") {
            Snackbar.make(view, "date is required", Snackbar.LENGTH_LONG).show()
            return false
        }else if(!(countdown_radiogroup.get(0) as RadioButton).isChecked  && !(countdown_radiogroup.get(1) as RadioButton).isChecked) {
            Snackbar.make(view, "countdown is required", Snackbar.LENGTH_LONG).show()
            return false
        }else
            return true
    }

    private fun onClickCancelButtonListener() {
        cancel_button.setOnClickListener { finish() }
    }


    fun onDataListener(){
        onDateSetListener=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            date_picker_text.text="$dayOfMonth."+(month+1)+".$year"
             }
    }

    fun showDataPicker(){
        val dataPickerDialog=DatePickerDialog(this,onDateSetListener,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        dataPickerDialog.show()
    }
}