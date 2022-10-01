package com.es.k_background_location_demo

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MyLocationService : Service() {


    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var locationClient: LocationClient
    override fun onBind(intent: Intent?): IBinder? {


        return null
    }

    override fun onCreate() {
        super.onCreate()

        locationClient = DefaultLocationClient(
            this,
            LocationServices.getFusedLocationProviderClient(applicationContext)

        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()

        }



        return super.onStartCommand(intent, flags, startId)
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    private fun start() {

        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking Location")
            .setContentText("Location")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient.getLocationUpdates(10000)
            .catch { cause -> cause.printStackTrace() }
            .onEach {
                val lat = it.latitude.toString().takeLast(3)
                val long = it.longitude.toString().takeLast(3)
                var currentAdreess = getAdressfromLatLong(it.latitude, it.longitude)
                val updateNotification = notification.setContentText("Location is $currentAdreess")
                notificationManager.notify(1, updateNotification.build())


            }.launchIn(
                serviceScope
            )


        startForeground(1, notification.build())


    }

    private fun getAdressfromLatLong(lat: Double, long: Double): String {

        var geocoder = Geocoder(this)
        var allAdress = geocoder.getFromLocation(lat, long, 1)

        var address: Address = allAdress[0]


        return address.getAddressLine(0)

    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


    companion object {

        const val ACTION_START = "action_start"
        const val ACTION_STOP = "action_stop"


    }


}