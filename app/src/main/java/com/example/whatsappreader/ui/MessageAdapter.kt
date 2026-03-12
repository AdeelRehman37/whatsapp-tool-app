package com.example.whatsappreader.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappreader.R
import com.example.whatsappreader.data.MessageEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(private var messages: List<MessageEntity>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName: TextView = view.findViewById(R.id.textContactName)
        val messageText: TextView = view.findViewById(R.id.textMessage)
        val timestamp: TextView = view.findViewById(R.id.textTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.id.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = messages[position]
        holder.contactName.text = msg.contactName
        holder.messageText.text = msg.messageText
        holder.timestamp.text = SimpleDateFormat("dd MMM yy, hh:mm a", Locale.getDefault())
            .format(Date(msg.timestamp))
    }

    override fun getItemCount() = messages.size

    fun updateData(newMessages: List<MessageEntity>) {
        messages = newMessages
        notifyDataSetChanged()
    }
}