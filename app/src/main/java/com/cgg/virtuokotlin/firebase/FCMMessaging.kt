package com.cgg.virtuokotlin.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.cgg.virtuokotlin.R
import com.cgg.virtuokotlin.Utilities.AppConstants
import com.cgg.virtuokotlin.Utilities.Utils
import com.cgg.virtuokotlin.application.VirtuoApplication
import com.cgg.virtuokotlin.ui.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.InputStream
import java.net.URL

class FCMMessaging : FirebaseMessagingService() {
    companion object {
        private const val TAG = "FCMMessaging"
        private val vibrate = longArrayOf(0, 100, 200, 300)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val preferencesEditor = VirtuoApplication.SharedPrefEditorObj.getPreferencesEditor()
        preferencesEditor!!.putString(AppConstants.FCM_TOKEN, token)
        preferencesEditor.commit()
        Log.i(TAG, "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isNotEmpty()) {
            val map: Map<String, String> = message.data
            val title: String? = map.get("title")
            val msg: String? = map.get("body")
            if (map.get("photopath") != null && !TextUtils.isEmpty(map.get("photopath"))) {
                val bm: Bitmap = getBitMapFromUrl(map.get("photopath")!!)
                sendNotification(title!!, msg!!, bm)
            } else {
                sendNotification(title!!, msg!!, null)
            }
        }
    }

    private fun getBitMapFromUrl(value: String): Bitmap {
        val stream: InputStream = URL(value).openStream()
        return BitmapFactory.decodeStream(stream)
    }

    private fun sendNotification(title: String, messageBody: String, result: Bitmap?) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = getString(R.string.virtuo_alerts)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val icon = BitmapFactory.decodeResource(resources, R.drawable.virtuo_circle_logo_bm)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        notificationBuilder.setFullScreenIntent(pendingIntent, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(getNotificationIcon())
            notificationBuilder.color = ContextCompat.getColor(this, R.color.color1)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.virtuo_notification_icon)
        }
        notificationBuilder.setStyle(
            NotificationCompat.BigTextStyle().bigText(messageBody)
        )
        if (result != null) {
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(result)
            )
                .setLargeIcon(result)
        } else {
            notificationBuilder.setLargeIcon(icon)
        }
        notificationBuilder
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(vibrate)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setColor(ContextCompat.getColor(this, R.color.color1))
            .setTicker(messageBody)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.virtuo_alerts),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(
            Utils.generateRandomNum() /* ID of notification */,
            notificationBuilder.build()
        )
    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.drawable.virtuo_notification_icon else R.drawable.virtuo_notification_icon
    }

}