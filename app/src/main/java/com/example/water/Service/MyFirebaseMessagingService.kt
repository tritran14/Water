package com.example.water.Service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import androidx.core.app.NotificationCompat
import com.example.water.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if(remoteMessage.data!=null){
            sendNotification(remoteMessage)
        }
    }
    fun sendNotification(remoteMessage: RemoteMessage){
        var data=remoteMessage.data
        val title=data.get("title")
        val content=data.get("content")
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID="WaterAppChannel"

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant")
            val notificationChannel=NotificationChannel(NOTIFICATION_CHANNEL_ID
                ,"MyNotification"
                ,NotificationManager.IMPORTANCE_MAX)
            notificationChannel.description="WaterApp description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor= Color.RED
            notificationChannel.vibrationPattern= longArrayOf(0,1000,5000,1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder=NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(false)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.small_icon)
            .setTicker("Hello")
            .setContentTitle(title)
            .setContentText(content)
            .setContentInfo("Info")
        notificationManager.notify(1,notificationBuilder.build())
    }
}