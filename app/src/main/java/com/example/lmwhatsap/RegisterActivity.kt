package com.example.lmwhatsap

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var login :TextView
    lateinit var signup:TextView
    lateinit var back:ImageView
    lateinit var userInput:EditText
    lateinit var emailInput:EditText
    lateinit var passwordInput:EditText
    lateinit var confirmInput:EditText
    lateinit var auth:FirebaseAuth
    lateinit var dbRef:DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        auth=FirebaseAuth.getInstance()
        dbRef=FirebaseDatabase.getInstance().getReference()
        userInput=findViewById(R.id.username)
        emailInput=findViewById(R.id.email)
        passwordInput=findViewById(R.id.password)
        confirmInput=findViewById(R.id.confirm)
        signup=findViewById(R.id.signup)

        signup.setOnClickListener {
            val username:String=userInput.getText().toString()
            val email:String=emailInput.getText().toString()
            val password:String=passwordInput.getText().toString()
            val confirm:String=confirmInput.getText().toString()

            if (username.isEmpty()){
                Toast.makeText(this,"username is required",Toast.LENGTH_LONG).show()
                userInput.setError("Username is required")
                userInput.requestFocus()
            }
            else if (email.isEmpty()){
                Toast.makeText(this,"email is required",Toast.LENGTH_LONG).show()
                emailInput.setError("Email is required")
                emailInput.requestFocus()
            }
            else if (password.isEmpty()){
                Toast.makeText(this,"Password is required",Toast.LENGTH_LONG).show()
                passwordInput.setError("Password is required")
                passwordInput.requestFocus()
            }
            else if (confirm.isEmpty()){
                Toast.makeText(this,"Confirm is required",Toast.LENGTH_LONG).show()
                confirmInput.setError("Confirm is required")
                confirmInput.requestFocus()
            }
            else if(password  !=confirm){
                Toast.makeText(this,"Password don't match",Toast.LENGTH_LONG).show()
                passwordInput.setError("Password don't match")
                passwordInput.requestFocus()
            }
            else{
                registerUser(username,email,password)
                signup.setText("Registering...")
                signup.isEnabled()
            }
        }




        back=findViewById(R.id.back)
        login=findViewById(R.id.login)
        login.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        back.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                run {
                    if (task.isSuccessful) {
                        addUserToDatabase(username,email,auth.currentUser?.uid)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        signup.setText("Sign Up")
                        Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun addUserToDatabase(username: String, email: String, uid: String?) {
        if (uid != null) {
            dbRef.child("users").child(uid).setValue(User(email,uid,username))
        }
    }
}