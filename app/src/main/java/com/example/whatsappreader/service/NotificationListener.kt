package com.example.whatsappreader.service

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.whatsappreader.data.AppDatabase
import com.example.whatsappreader.data.MessageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationListener : NotificationListenerService() {
    private val TAG = "NotificationListener"

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName == "com.whatsapp") {
            val extras = sbn.notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE)
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()

            if (title != null && text != null && title != "WhatsApp") {
                Log.d(TAG, "WhatsApp Message received - Contact: $title, Text: $text")
                saveMessage(title, text)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Not used
    }

    private fun saveMessage(contact: String, message: String) {
        val db = AppDatabase.getDatabase(applicationContext)
        val entity = MessageEntity(
            contactName = contact,
            messageText = message,
            timestamp = System.currentTimeMillis()
        )
        CoroutineScope(Dispatchers.IO).launch {
            db.messageDao().insertMessage(entity)
        }
    }
}