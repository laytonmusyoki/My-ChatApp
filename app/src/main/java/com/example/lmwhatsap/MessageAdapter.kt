package com.example.lmwhatsap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context,val messageList:ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE=1
    val ITEM_SENT=2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1){
            val view:View=LayoutInflater.from(parent.context).inflate(R.layout.receiver_message,parent,false)
            return ReceiveViewHolder(view)
        }
        else{
            val view:View=LayoutInflater.from(parent.context).inflate(R.layout.sender_message,parent,false)
            return SendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage=messageList[position]
        if (holder.javaClass==SendViewHolder::class.java){
            val viewHolder=holder as SendViewHolder
            holder.sendMessage.text=currentMessage.message
        }
        else{
            val viewHolder=holder as ReceiveViewHolder
            holder.ReceiveMessage.text=currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid==currentMessage.senderId){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }
    }


    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMessage:TextView=itemView.findViewById(R.id.message)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ReceiveMessage:TextView=itemView.findViewById(R.id.message)
    }
}