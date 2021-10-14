package com.hyejineee.pushannouncer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)

        createNotiChannel()

        val type = msg.data["type"]?.let {
            NotificationType.valueOf(it)
        }
        val title = msg.data["title"] ?: "NoTitle"
        val m = msg.data["msg"] ?: "No Message"

        type ?: return


        NotificationManagerCompat.from(this)
            .notify(type.id, createNoti(type, title, m))

    }

    private fun createNotiChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT,
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNoti(type: NotificationType, title: String, msg: String): Notification {

        val i = Intent(this, MainActivity::class.java).apply {
            putExtra("type", type.title)
            Log.d("type", type.title)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val p = PendingIntent.getActivity(this, type.id, i, FLAG_UPDATE_CURRENT)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(p)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                builder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("😀 😃 😄 😁 😆 😅 😂 🤣 🥲 ☺️ 😊 😇 🙂 🙃 😉 😌 😍 🥰 😘 😗 😙 😚 😋 😛 😝 😜 🤪 🤨 🧐 🤓 😎 🥸 🤩 🥳 😏 😒 😞 😔 😟 😕 🙁 ☹️ 😣 😖 😫 😩 🥺 😢 😭 😤 😠 😡 🤬 🤯 😳 🥵 🥶 😱 😨 😰 😥 😓")
                )
            }
            NotificationType.CUSTOM -> {
                builder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.custom_notiview
                        ).apply {
                            setTextViewText(R.id.customNotiTitleTextView, title)
                            setTextViewText(R.id.customNotiSubTextView, msg)
                        })
            }
        }

        return builder.build()
    }

    companion object {
        private const val CHANNEL_NAME = "test noti"
        private const val CHANNEL_DESCRIPTION = "this is noti test"
        private const val CHANNEL_ID = "channel id"
    }
}