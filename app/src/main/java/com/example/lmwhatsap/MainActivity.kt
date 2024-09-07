package com.example.lmwhatsap

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var auth:FirebaseAuth

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if (id==R.id.logout){
            auth=FirebaseAuth.getInstance()
            auth.signOut()
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        toolbar=findViewById(R.id.toolbar)
        drawerLayout=findViewById(R.id.draweLayout)
        bottomNavigationView=findViewById(R.id.bottom_nav)

        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ChatsFragment()).commit()


        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("LM Whatsapp")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        var selectedFragment:Fragment?=null
        if(id==R.id.chats){
            selectedFragment=ChatsFragment()
        }
        else if (id==R.id.calls){
            selectedFragment=CallsFragment()
        }
        if(selectedFragment !=null){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment).commit()
        }
        return true
    }
}