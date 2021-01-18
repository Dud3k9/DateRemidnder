package com.Dudek9.datereminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import java.text.SimpleDateFormat
import java.util.*

class Notification() {


    fun setReminders(
        context: Context,
        date: com.Dudek9.datereminder.roomDatabase.Date,
        nextPeriod: Boolean
    ) {
        val notificationDateInMilis = timeLeft(date.date, date.countdown,nextPeriod)

        if (isMonthReminder(date.reminder)) {
            setNotificationOnDate(
                context,
                "${date.name} in a month",
                date,
                (notificationDateInMilis - 30 * 86400000)
            )
        }
        if (isWeekReminder(date.reminder)) {
            setNotificationOnDate(
                context,
                "${date.name} in a week",
                date,
                notificationDateInMilis - 7 * 86400000
            )
        }
        if (isDayReminder(date.reminder)) {
            setNotificationOnDate(
                context,
                "${date.name} tomorrow",
                date,
                notificationDateInMilis - 1 * 86400000
            )
        }

    }

    lateinit var myIntent: Intent;
    lateinit var alarmIntent: PendingIntent;
    fun setNotificationOnDate(
        context: Context,
        name: String,
        date: com.Dudek9.datereminder.roomDatabase.Date,
        dateInMilis: Long
    ) {
        myIntent = Intent(
            context.applicationContext,
            BroadastNotification::class.java
        ).putExtra("name", name)
        var bundle: Bundle? = Bundle()
        bundle?.putSerializable("date", date)
        myIntent.putExtra("bundle", bundle)

        Log.d("MYLOGS", name + date.date_id)

        alarmIntent = PendingIntent.getBroadcast(
            context.applicationContext, date.date_id,
            myIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager =
            getSystemService(context.applicationContext, AlarmManager::class.java) as AlarmManager


        alarmManager.set(AlarmManager.RTC_WAKEUP, dateInMilis, alarmIntent)
    }


    private fun timeLeft(date: String, countdown: Int,next: Boolean): Long {

        val format = SimpleDateFormat("d.M.yyyy")
        val dateformated = format.parse(date)
        val notifyHour=18
        var diff = 0L
        if (countdown == 0) {
            if (next || Date(
                    format.parse(format.format(Calendar.getInstance().time)).year,
                    dateformated.month,
                    dateformated.date
                ).before(format.parse(format.format(Calendar.getInstance().time)))
            ) {
                diff = Date(
                    format.parse(format.format(Calendar.getInstance().time)).year + 1,
                    dateformated.month,
                    dateformated.date,notifyHour-1,0
                ).time
            } else
                diff = Date(
                    format.parse(format.format(Calendar.getInstance().time)).year,
                    dateformated.month,
                    dateformated.date,notifyHour-1,0
                ).time
        } else
            if (next || Date(
                    format.parse(format.format(Calendar.getInstance().time)).year,
                    format.parse(format.format(Calendar.getInstance().time)).month,
                    dateformated.date
                ).before(format.parse(format.format(Calendar.getInstance().time)))
            ) {
                if(format.parse(format.format(Calendar.getInstance().time)).month==11){
                    diff = Date(
                        format.parse(format.format(Calendar.getInstance().time)).year+1,
                        0,
                        dateformated.date,notifyHour-1,0).time
                }else {
                    diff = Date(
                        format.parse(format.format(Calendar.getInstance().time)).year,
                        format.parse(format.format(Calendar.getInstance().time)).month + 1,
                        dateformated.date,notifyHour-1,0
                    ).time
                }
            } else {
                diff = Date(
                    format.parse(format.format(Calendar.getInstance().time)).year,
                    format.parse(format.format(Calendar.getInstance().time)).month,
                    dateformated.date,notifyHour-1,0
                ).time
            }
        return diff

    }


    fun isMonthReminder(reminders: Int): Boolean {
        if (reminders % 1000 >= 100)
            return true
        return false
    }

    fun isWeekReminder(reminders: Int): Boolean {
        if (reminders % 100 >= 10)
            return true
        return false
    }

    fun isDayReminder(reminders: Int): Boolean {
        if (reminders % 10 >= 1)
            return true
        return false

    }

}


class BroadastNotification : BroadcastReceiver() {

    val ID = "CHANEL_ID"
    val CHANEL_NAME = "CHANEL_NAME"

    override fun onReceive(context: Context?, intetnt: Intent?) {

        var date =
            (intetnt?.extras?.get("bundle") as Bundle).getSerializable("date") as com.Dudek9.datereminder.roomDatabase.Date
        var name = intetnt?.extras?.get("name").toString()

        sendNotification(context, name)
        notifyForNextPeriod(context, date)
        Log.d("MYLOGS", "brodcast ")
    }

    private fun notifyForNextPeriod(
        context: Context?,
        date: com.Dudek9.datereminder.roomDatabase.Date?
    ) {
        if (context != null && date != null) {
            Notification().setReminders(context, date,true)
        }
    }

    private fun sendNotification(context: Context?, name: String) {
        Log.d("MYLOGS", "notify")

        var notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java), 0
        )

        var channelNotification = NotificationCompat.Builder(context, ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Important date")
            .setContentText(name)

            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(ID, CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(1, channelNotification.build())
        }else {
            notificationManager.notify(1, channelNotification.build())
        }
    }


}



