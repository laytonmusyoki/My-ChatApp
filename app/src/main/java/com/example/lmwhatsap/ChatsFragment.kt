package com.example.lmwhatsap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.Visibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatsFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var userAdapter: UserAdapter
    lateinit var userList: ArrayList<User>
    lateinit var auth:FirebaseAuth
    lateinit var dbRef:DatabaseReference
    lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_chats, container, false)
        recyclerView=view.findViewById(R.id.recycler)
        progressBar=view.findViewById(R.id.progress_circular)
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout)
        userList=ArrayList()

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing=true
            initData()
        }

        progressBar.visibility=View.VISIBLE
        initData()
        initRecycler()
        return view
    }

    private fun initData() {
        auth=FirebaseAuth.getInstance()
        dbRef=FirebaseDatabase.getInstance().getReference()
        dbRef.child("users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)
                    //Toast.makeText(context,"${currentUser}",Toast.LENGTH_LONG).show()
                    if (currentUser != null) {
                        if(currentUser.uid !=auth.currentUser?.uid){
                            userList.add(currentUser!!)
                            progressBar.visibility=View.GONE
                        }
                    }

                }
                userAdapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing=false
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun initRecycler() {
        recyclerView.layoutManager=LinearLayoutManager(context)
        userAdapter=UserAdapter(context,userList)
        recyclerView.adapter=userAdapter
        userAdapter.notifyDataSetChanged()
    }


}