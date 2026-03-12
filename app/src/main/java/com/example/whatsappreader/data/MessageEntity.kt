package com.example.whatsappreader.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contactName: String,
    val messageText: String,
    val timestamp: Long
)