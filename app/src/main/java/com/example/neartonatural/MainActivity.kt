package com.example.neartonatural

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.neartonatural.ui.account.SignUpActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


    
        val btnSignin : Button = findViewById(R.id.btnSignin)
        btnSignin.setOnClickListener{

           val intent = Intent(this,LoggedActivity::class.java)
            startActivity(intent)
        }

        val btnSignup : Button = findViewById(R.id.btnSignup)
        btnSignup.setOnClickListener{

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        

    }
}
