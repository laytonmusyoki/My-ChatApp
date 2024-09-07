package com.example.lmwhatsap

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var signup:TextView
    lateinit var emailInput:EditText
    lateinit var passwordInput:EditText
    lateinit var login:TextView
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        auth=FirebaseAuth.getInstance()
        login=findViewById(R.id.login)
        emailInput=findViewById(R.id.email)
        passwordInput=findViewById(R.id.password)
        signup=findViewById(R.id.signup)
        signup.setOnClickListener {
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            val email=emailInput.text.toString()
            val password=passwordInput.text.toString()
            if(email.isEmpty()){
                emailInput.setError("Username is required")
                emailInput.requestFocus()
            }
            else if(password.isEmpty()){
                passwordInput.setError("Password is required")
                passwordInput.requestFocus()
            }
            else{
                loginUser(email,password)
                login.setText("Logging...")
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{t->
                run {
                    if (t.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this,"You have entered wrong Credentials",Toast.LENGTH_LONG).show()
                        login.setText("Login")
                    }
                }

            }
    }
}