package com.example.neartonatural.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.neartonatural.R
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplication.MySingleton
import com.example.neartonatural.MainActivity
import kotlinx.android.synthetic.main.fragment_account.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_account)

        btnRegister.setOnClickListener {
            saveUser()
        }
        btnReset.setOnClickListener{
            txtUsername.setText("")
            editTextContact.setText("")
            txtPassword.setText("")
            txtConPassword.setText("")
        }
    }
    private fun saveUser()
    {
        val name = txtUsername.text.toString()
        val contact = editTextContact.text.toString()
        val password = txtPassword.text.toString()
        val conPassword = txtConPassword.text.toString()

        if(TextUtils.isEmpty(txtUsername.text)){
            txtUsername.setError(getString(R.string.empty_username))
        }
        else{
            txtUsername.setError(null);
        }
        if(TextUtils.isEmpty(editTextContact.text)){
            editTextContact.setError(getString(R.string.empty_contact))
        }
        else{
            editTextContact.setError(null);
        }
        if(TextUtils.isEmpty(txtPassword.text)){
            txtPassword.setError(getString(R.string.empty_password))
        }
        else{
            txtPassword.setError(null);
        }
        if(TextUtils.isEmpty(txtConPassword.text)){
            txtConPassword.setError(getString(R.string.empty_conPassword))
        }
        else{
            txtConPassword.setError(null);
        }

        if(name.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please fill in the name.",
                Toast.LENGTH_LONG
            ).show()
        }
        else if(contact.isEmpty()){
                Toast.makeText(
                    applicationContext,
                    "Please fill in the contact.",
                    Toast.LENGTH_LONG
                ).show()
        }
        else if(password.isEmpty()){
                Toast.makeText(
                    applicationContext,
                    "Please fill in the password.",
                    Toast.LENGTH_LONG
                ).show()
        }
        else if(conPassword.isEmpty()){
                Toast.makeText(
                    applicationContext,
                    "Please fill in the name.",
                    Toast.LENGTH_LONG
                ).show()
        }
        else
        {
            if (password==conPassword) {
                if(password.length>5) {
                    createUser(User(name, password, contact))
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(
                        applicationContext,
                        "Password length must more than 5.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else
            {
                Toast.makeText(
                    applicationContext,
                    "Confirm Password is not same with Password.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun createUser(user: User) {
        val url =
            getString(R.string.url_server) + getString(R.string.url_user_create) + "?name=" + user.name +
                     "&password=" + user.password +"&contact=" + user.contact


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()

                        if (success.equals("1")) {
                            Toast.makeText(
                                applicationContext,
                                "Record saved",
                                Toast.LENGTH_LONG
                            ).show()
                            //Add record to user list
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Record not saved",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Main", "Response: %s".format(e.message.toString()))

                }
            },
            Response.ErrorListener { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))
            }
        )

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}

