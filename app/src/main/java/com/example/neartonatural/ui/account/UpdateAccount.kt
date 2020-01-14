package com.example.neartonatural.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neartonatural.R
import com.example.neartonatural.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_account.*

class UpdateAccount : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)


    }
    private fun update() {
        val name = txtUsername.text.toString()
        val contact = editTextContact.text.toString()
        val password = txtPassword.text.toString()
        val conPassword = txtConPassword.text.toString()

        if (TextUtils.isEmpty(txtUsername.text)) {
            txtUsername.setError(getString(R.string.empty_username))
        } else {
            txtUsername.setError(null)
            validateUser()
        }
        if (TextUtils.isEmpty(editTextContact.text)) {
            editTextContact.setError(getString(R.string.empty_contact))
        } else {
            editTextContact.setError(null)
        }
        if (TextUtils.isEmpty(txtPassword.text)) {
            txtPassword.setError(getString(R.string.empty_password))
        } else {
            txtPassword.setError(null)
        }

        if (name.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please fill in the name.",
                Toast.LENGTH_LONG
            ).show()
        } else if (contact.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please fill in the contact.",
                Toast.LENGTH_LONG
            ).show()
        } else if (password.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please fill in the password.",
                Toast.LENGTH_LONG
            ).show()
        } else {
                if (password.length > 5) {
                    updateUser(User(name, password, contact))
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Password length must more than 5.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        private fun updateUser(user: User) {
            val url =
                getString(R.string.url_server) + getString(R.string.url_user_update) + "?name=" + user.name + "&password=" + user.password + "&contact=" + user.contact
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
                                    "Update Successful",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                //Add record to user list
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Update Unsuccessful",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("Main", "Response: %s".format(e.message.toString()))

                    }
                },
                Response.ErrorListener { error ->
                    Log.i("Main", "Response: %s".format(error.message.toString())).toString()
                    Log.d("Main", "Response: %s".format(error.message.toString())).toString()
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
    private fun validateUser(){
        val url =
            getString(R.string.url_server) + getString(R.string.url_validate_Name) + "?name=" + txtUsername.text.toString()
        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()
                        if (success.equals("1")) {
                            txtUsername.setError(getString(R.string.error_value_repeated))
                            Toast.makeText(
                                applicationContext,
                                "This username already registered!!! Try Again.",
                                Toast.LENGTH_LONG
                            ).show()
                            //Add record to user list
                        }
                        else
                        {
                            txtUsername.setError(null)
                            update()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Main", "Response: %s".format(e.message.toString()))

                }
            },
            Response.ErrorListener { error ->
                Log.i("Main", "Response: %s".format(error.message.toString())).toString()
                Log.d("Main", "Response: %s".format(error.message.toString())).toString()
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