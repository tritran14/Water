package com.example.water

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.math.abs
import kotlin.system.measureTimeMillis

class MainActivity : MyApplication() {
    var s0: Float=0f
    var v: Float=0f
    var prevTime=System.currentTimeMillis()
    val infoViewModel: InfoViewModel by lazy{
        ViewModelProvider(
            this,
            InfoViewModel.InfoViewModelFactory(this.application)
        )[InfoViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            icon.shadowColor=getColor(R.color.colorPrimary)
//        }


        CoroutineScope(Dispatchers.Default).launch {
            val time = System.currentTimeMillis()
            var i=0
            while (true){
                runBlocking {
                    val val1=async {
                        withContext(Main){
                            getInfo1()
                            Log.d("AAA1","time : ${System.currentTimeMillis()-time} ms")
                        }
                    }
                    val val2=async {
                        withContext(Main){
                            getInfo2()
                            Log.d("AAA2","time : ${System.currentTimeMillis()-time} ms")
                        }
                    }
                    Log.d("AAA1","2#### : ${val1.await()+val2.await()} ms")
                    calc(val1.await()+val2.await())
                }
//                withContext(Main){
//                    async {
//                        getInfo1()
//                    }
//                    async {
//                        getInfo2()
//                    }
//
//                }
                Log.d("AAA1","Update again")
                ++i
                delay(10000)
            }
        }

    }
    suspend fun getInfo1(){
        infoViewModel.getInfoFromApi1().observe(this,{
            it?.let {resource->
                when(resource.status){
                    Status.SUCCESS->{
                        var cur=resource.data
                        if(cur!=null){
                            level.text=String.format("%.5f cm",cur.feeds[0].field1)
                            s0=cur.feeds[0].field1 // lay s0 tu api
                            if(s0==-1f){
                                icon.setImageBitmap(
                                    BitmapFactory.decodeResource(resources, R.drawable.dry)
                                )
                                icon.shadowColor=Color.YELLOW
                                level.text=String.format("%.5f cm",0f)
                                state.text="Khô ráo"

                            }
                            else if (s0==-2f){
                                icon.setImageBitmap(
                                    BitmapFactory.decodeResource(resources, R.drawable.sap_mua)
                                )
                                icon.shadowColor=Color.BLUE
                                level.text=String.format("%.5f cm",0f)
                                state.text="Bắt đầu mưa"
                            }
                            else{
                                icon.setImageBitmap(
                                    BitmapFactory.decodeResource(resources, R.drawable.rain)
                                )
                                icon.shadowColor=Color.GREEN
                                level.text=String.format("%.5f cm",cur.feeds[0].field1)
                                state.text="Mưa"
                            }
//                            setUISpeed(cur.feeds[0].field2)
//                            setUILevel(cur.channel.field1)
//                            }
                        }
                    }
                    Status.ERROR->{
                        Toast.makeText(this, "error : "+it.message, Toast.LENGTH_SHORT).show()
                        Log.d("AAA1","error : "+it.message)
                    }
                    Status.LOADING->{
                        Log.d("AAA1","loading")
                    }
                }
            }
        })
    }
    suspend fun getInfo2(){
        infoViewModel.getInfoFromApi2().observe(this,{
            it?.let {resource->
                when(resource.status){
                    Status.SUCCESS->{
                        var cur=resource.data
                        Log.d("AAA2","comeon : "+resource.data)
                        if(cur!=null){
                            Log.d("AAA2","i'm here")
//                            CoroutineScope(Main).launch {
//                                setData(cur?.feeds[0].field2,cur?.channel.field1)
                            speed.text=String.format("%.5f cm/s",cur.feeds[0].field2)
                            v=cur.feeds[0].field2
//                            setUISpeed(cur.feeds[0].field2)
//                            setUILevel(cur.channel.field1)
//                            }
                        }
                    }
                    Status.ERROR->{
                        Toast.makeText(this, "error : "+it.message, Toast.LENGTH_SHORT).show()
                        Log.d("AAA1","error : "+it.message)
                    }
                    Status.LOADING->{

                    }
                }
            }
        })
    }
    fun sendNotification(){
        val title="Báo động"
        val content="Dự báo có lũ lụt, yêu cầu di tản"
//        20s bao 1 lan
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID="WaterAppChannel"

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant")
            val notificationChannel= NotificationChannel(NOTIFICATION_CHANNEL_ID
                ,"MyNotification"
                , NotificationManager.IMPORTANCE_MAX)
            notificationChannel.description="WaterApp description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor= Color.RED
            notificationChannel.vibrationPattern= longArrayOf(0,1000,5000,1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder= NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
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

    suspend fun calc(num: Int){ // hang xu ly 20s kiem tra du doan lu lut
        var timee=System.currentTimeMillis()-prevTime
        Log.d("AAA1","s0 : $s0 | v : $v")
        Log.d("AAA1","timee : ${timee}")
        if(abs(timee-20000)<=1000){ // neu dung 20s
            var s=s0+v*30*60 // du doan muc nuoc trong 30p toi bang cong thuc
            if(s>480f){ // neu vuot qua nguoi
                sendNotification() // gui thong bao
                s0=0f // reset bien
                v=0f
            }
            Log.d("AAA1_calc","s : $s")
            withContext(Main){
//                Toast.makeText(this@MainActivity, "Updateeeee", Toast.LENGTH_SHORT).show()
            }
            prevTime=System.currentTimeMillis() // gan lai thoi gian truoc do
        }
    }
//    fun setData(val_speed: String,val_level: String){
//        Log.d("AAA1","set data")
//        speed.text=val_speed.toString()+" cm/s"
//        level.text=val_level.toString()+" cm"
//    }

}
