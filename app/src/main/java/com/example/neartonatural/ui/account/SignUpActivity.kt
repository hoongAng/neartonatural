package com.example.neartonatural.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.neartonatural.R
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.fragment_account.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_account)

        btnRegister.setOnClickListener {

            //if(name==""||contact==""||password==""){
            //    val intent = Intent(this, SignUpActivity::class.java)
            //    startActivity(intent)
           // }
            //else
           // {
             //   if (password!=conPassword)
             //   {
                    saveUser()
            //    }
             //   else
            //    {
            //        val intent = Intent(this, SignUpActivity::class.java)
             //       startActivity(intent)
            //    }
            //}
        }
    }

    private fun saveUser(){
        val name = txtUsername.text.toString()
        val contact = editTextContact.text.toString()
        val password = txtPassword.text.toString()

        if (TextUtils.isEmpty(txtUsername.text)) {
            txtUsername.setError(getString(R.string.error_value_required))
        }
        if (TextUtils.isEmpty(editTextContact.text)) {
            editTextContact.setError(getString(R.string.error_value_required))
        }
        if (TextUtils.isEmpty(txtPassword.text)) {
            txtPassword.setError(getString(R.string.error_value_required))
        }
        fun createUser(user: User) {
            val url =
                getString(R.string.url_server) + getString(R.string.url_user_create) + "?name=" + user.name +
                        "&contact=" + user.contact + "&password=" + user.password

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
        }
        createUser(User(name, password, contact))
    }
}

