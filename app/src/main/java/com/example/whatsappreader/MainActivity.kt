package com.example.whatsappreader

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappreader.data.AppDatabase
import com.example.whatsappreader.ui.MessageAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(emptyList())
        recyclerView.adapter = adapter

        val btnPermission = findViewById<Button>(R.id.btnPermission)
        btnPermission.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        loadMessages()
    }

    override fun onResume() {
        super.onResume()
        if (!isNotificationServiceEnabled()) {
            findViewById<Button>(R.id.btnPermission).text = "Enable Notification Access"
            findViewById<Button>(R.id.btnPermission).setBackgroundColor(resources.getColor(android.R.color.holo_red_dark, theme))
        } else {
            findViewById<Button>(R.id.btnPermission).text = "Notification Access Granted"
            findViewById<Button>(R.id.btnPermission).setBackgroundColor(resources.getColor(android.R.color.holo_green_dark, theme))
        }
    }

    private fun loadMessages() {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            db.messageDao().getAllMessages().collect { messages ->
                adapter.updateData(messages)
            }
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":")
            for (name in names) {
                val cn = ComponentName.unflattenFromString(name)
                if (cn != null && TextUtils.equals(pkgName, cn.packageName)) {
                    return true
                }
            }
        }
        return false
    }
}