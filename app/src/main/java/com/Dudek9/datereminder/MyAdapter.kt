package com.Dudek9.datereminder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Dudek9.datereminder.roomDatabase.Date
import kotlinx.android.synthetic.main.date_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyAdapter(private val context: Context, private var listOfDate: List<Date>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dateRow = layoutInflater.inflate(R.layout.date_row, parent, false)
        return MyViewHolder(dateRow)
    }

    override fun getItemCount(): Int = listOfDate.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = holder.view.date_name_text
        val date = holder.view.date_text
        val left = holder.view.day_left_text
        val ago = holder.view.day_ago_text
        val edit = holder.view.edit_buton

        sortDateList()

        name.text = listOfDate[position].name
        date.text = listOfDate[position].date

        edit.setOnClickListener {
            val intent = Intent(context, EditDateActivity::class.java)
            intent.putExtra("date", listOfDate[position])
            context.startActivity(intent)
        }

        ago.text = timeAgo(listOfDate[position].date)

        left.text = timeLeft(
            listOfDate[position].date,
            listOfDate[position].countdown
        ).toString() + " days left"
    }

    private fun sortDateList(){
        listOfDate = listOfDate.sortedBy({ timeLeft(it.date, it.countdown) })
    }

    private fun timeLeft(date: String, countdown: Int): Long {
        val format = SimpleDateFormat("d.M.yyyy")
        val dateformated = format.parse(date)
        var diff = 0L
        if (countdown == 0) {
            if (Date(
                    format.parse(format.format(Calendar.getInstance().time)).year,
                    dateformated.month,
                    dateformated.date
                ).before(format.parse(format.format(Calendar.getInstance().time)))
            ) {
                diff = Date(
                    format.parse(format.format(Calendar.getInstance().time)).year + 1,
                    dateformated.month,
                    dateformated.date
                ).time - format.parse(format.format(Calendar.getInstance().time)).time
            } else
                diff = Date(
                    format.parse(format.format(Calendar.getInstance().time)).year,
                    dateformated.month,
                    dateformated.date
                ).time - format.parse(format.format(Calendar.getInstance().time)).time
        } else
            if (Date(
                    format.parse(format.format(Calendar.getInstance().time)).year,
                    format.parse(format.format(Calendar.getInstance().time)).month,
                    dateformated.date
                ).before(format.parse(format.format(Calendar.getInstance().time)))
            ) {
                diff = Date(
                    format.parse(format.format(Calendar.getInstance().time)).year,
                    format.parse(format.format(Calendar.getInstance().time)).month + 1,
                    dateformated.date
                ).time - format.parse(format.format(Calendar.getInstance().time)).time
            } else {
                diff = Date(
                    format.parse(format.format(Calendar.getInstance().time)).year,
                    format.parse(format.format(Calendar.getInstance().time)).month,
                    dateformated.date
                ).time - format.parse(format.format(Calendar.getInstance().time)).time
            }
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

    }

    private fun timeAgo(date: String): String {
        val format = SimpleDateFormat("d.M.yyyy")
        var diff =
            format.parse(format.format(Calendar.getInstance().time)).time - format.parse(date).time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString() + " days ago"
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)