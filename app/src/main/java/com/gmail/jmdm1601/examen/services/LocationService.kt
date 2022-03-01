package com.gmail.jmdm1601.examen.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.gmail.jmdm1601.examen.R
import com.gmail.jmdm1601.examen.core.Constants
import com.gmail.jmdm1601.examen.data.remote.FirebaseService
import com.gmail.jmdm1601.examen.helpers.LocationHelper
import com.google.firebase.firestore.GeoPoint

class LocationService : Service() {
    override fun onCreate() {
        super.onCreate()
        isServiceStarted = true
        val builder =
            NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setContentTitle(getString(R.string.location))
                .setContentText(getString(R.string.notification_content))
                .setSmallIcon(R.drawable.ic_location_on)

        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = Constants.NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        startForeground(1, builder.build())

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        LocationHelper().startListeningUserLocation(
            this, object : LocationHelper.MyLocationListener {
                override fun onLocationChanged(location: Location?) {
                    mLocation = location
                    mLocation?.let {
                        FirebaseService.addLocation(
                            GeoPoint(
                                mLocation!!.latitude,
                                mLocation!!.longitude
                            )
                        )
                    }
                }
            })
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted = false
    }

    companion object {
        var mLocation: Location? = null
        var isServiceStarted = false
    }
}