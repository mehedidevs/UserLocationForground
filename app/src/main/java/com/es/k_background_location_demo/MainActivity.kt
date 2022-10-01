package com.es.k_background_location_demo

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        setContentView(R.layout.activity_main)


    }

    fun start(view: View) {
        Intent(applicationContext, MyLocationService::class.java).apply {
            action = MyLocationService.ACTION_START
            startService(this)

        }

    }

    fun stop(view: View) {

        Intent(applicationContext, MyLocationService::class.java).apply {
            action = MyLocationService.ACTION_STOP
            startService(this)

        }


    }
}