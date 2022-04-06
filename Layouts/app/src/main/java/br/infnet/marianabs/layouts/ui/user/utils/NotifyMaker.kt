package br.infnet.marianabs.layouts.ui.user.utils


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.infnet.marianabs.layouts.MainActivity
import br.infnet.marianabs.layouts.R

private const val CHANNEL_ID = "book_channel_id"
private const val NOTIFICATION_ID = 1

class NotificationHelper(val context: Context) {
    fun createNotification(title: String, message: String){
        notifyBookerChannel()

        val intent= Intent(context, MainActivity::class.java).apply {
            flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val intented = PendingIntent.getActivity(context,0,intent,0)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_2)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(intented)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notification)

    }

    private fun notifyBookerChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_ID, importance).apply {
                description = "Books...."
            }
            val manager =
                (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                    createNotificationChannel(channel)
                }
        }

    }
}

