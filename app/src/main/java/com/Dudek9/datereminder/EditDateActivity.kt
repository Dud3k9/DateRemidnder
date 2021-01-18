package com.Dudek9.datereminder

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.view.get
import com.Dudek9.datereminder.roomDatabase.Date
import com.Dudek9.datereminder.roomDatabase.DateRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_create_date.cancel_button
import kotlinx.android.synthetic.main.activity_create_date.countdown_radiogroup
import kotlinx.android.synthetic.main.activity_create_date.date_picker_text
import kotlinx.android.synthetic.main.activity_create_date.day_earlier_checkbox
import kotlinx.android.synthetic.main.activity_create_date.month_earlier_checkbox
import kotlinx.android.synthetic.main.activity_create_date.name_edittext
import kotlinx.android.synthetic.main.activity_create_date.ok_button
import kotlinx.android.synthetic.main.activity_create_date.week_earlier_checkbox
import kotlinx.android.synthetic.main.activity_edit_date.*
import java.util.*

class EditDateActivity : AppCompatActivity() {

    lateinit var onDateSetListener: DatePickerDialog.OnDateSetListener
    val dateRepository by lazy{ DateRepository(this.application) }
    val date by lazy { intent.extras?.getSerializable("date") as Date }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_date)
        supportActionBar?.hide()

        initFields()
        onDataListener()
        date_picker_text.setOnClickListener { showDataPicker() }
        onClickCancelButtonListener()
        onClickOkButtonListener()
        onClickRemoveButtonListener()
    }

    private fun initFields() {
        name_edittext.setText(date.name)
        date_picker_text.setText(date.date)
        if(date.countdown==0)
            (countdown_radiogroup[0] as RadioButton).isChecked=true
        else
            (countdown_radiogroup[1] as RadioButton).isChecked=true
        if(date.reminder%1000>=100)
            month_earlier_checkbox.isChecked=true
        if(date.reminder%100>=10)
            week_earlier_checkbox.isChecked=true
        if(date.reminder%10>=1)
            day_earlier_checkbox.isChecked=true

    }

    private fun onClickRemoveButtonListener() {
        remove_button.setOnClickListener{
            dateRepository.deleteDate(date)
            finish()
        }
    }

    private fun onClickOkButtonListener() {
        ok_button.setOnClickListener {
            if(checkRequired(it)){
                date.name=name_edittext.text.toString()
                date.date=date_picker_text.text.toString()
                date.countdown=if((countdown_radiogroup.get(0) as RadioButton).isChecked) 0 else 1
                date.reminder=checkReminderCheckbox()
                dateRepository.updateDate(date)
                Notification().setReminders(this,date,false)//TODO testing notifications
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