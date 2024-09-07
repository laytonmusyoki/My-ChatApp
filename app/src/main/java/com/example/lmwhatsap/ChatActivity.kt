package com.example.lmwhatsap

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList:ArrayList<Message>
    lateinit var messageData:EditText
    lateinit var submitMessage: ImageView
    lateinit var dbRef:DatabaseReference
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    var ReceiverRoom:String?=null
    var SenderRoom:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
        recyclerView=findViewById(R.id.recyclerview)
        messageData=findViewById(R.id.messageData)
        submitMessage=findViewById(R.id.submitMessage)
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout)


        val username=intent.getStringExtra("name")
        val ReceiverUid=intent.getStringExtra("uid")
        val SenderUid=FirebaseAuth.getInstance().currentUser?.uid

        SenderRoom=ReceiverUid + SenderUid
        ReceiverRoom=SenderUid + ReceiverUid
        dbRef=FirebaseDatabase.getInstance().getReference()


        submitMessage.setOnClickListener{
            val message:String=messageData.text.toString()
            val messageObject=Message(message,SenderUid)

            dbRef.child("chats").child(SenderRoom!!).child("messages").push()
                .setValue(messageObject)
                .addOnSuccessListener {
                    dbRef.child("chats").child(ReceiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageData.setText("")
        }


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        messageList=ArrayList()



        // Enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(username)

        initData()
        initRecycler()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initData() {
        dbRef.child("chats").child(SenderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun initRecycler() {
        recyclerView.layoutManager=LinearLayoutManager(this)
        messageAdapter=MessageAdapter(this,messageList)
        recyclerView.adapter=messageAdapter
        messageAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
