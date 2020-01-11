package com.example.neartonatural.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neartonatural.LoggedActivity
import com.example.neartonatural.MySingleton
import com.example.neartonatural.R
import kotlinx.android.synthetic.main.login.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        val btnSignup : Button = findViewById(R.id.btnSignup)
        val btnSignin : Button = findViewById(R.id.btnSignin)
        val loginUser = txtUsername.text.toString()
        val loginPassword = txtPassword.text.toString()

        btnSignin.setOnClickListener{

           val intent = Intent(this, LoggedActivity::class.java)
            startActivity(intent)
        }


        btnSignup.setOnClickListener{

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun validateLogin(user: User) {
        val url = getString(R.string.url_server) + getString(R.string.url_user_create) + "?name=" + user.name + "&password=" + user.password +"&contact=" + user.contact
        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val success: String = response.get("success").toString()

                        if(success.equals("1")){
                            Toast.makeText(applicationContext, "Registration Successful", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            //Add record to user list
                        }else{
                            Toast.makeText(applicationContext, "Registration Unsuccessful", Toast.LENGTH_LONG).show()
                        }
                    }
                }catch (e:Exception){
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
