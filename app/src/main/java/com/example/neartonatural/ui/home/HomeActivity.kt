package com.example.neartonatural.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neartonatural.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        var user:String? = intent.getStringExtra("loggedId")
        text_home.setText(user)
    }


}