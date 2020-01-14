package com.example.neartonatural.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neartonatural.R
import com.example.neartonatural.ui.home.HomeActivity
import com.example.neartonatural.ui.posting.HidedActivity
import kotlinx.android.synthetic.main.login.txtPassword
import kotlinx.android.synthetic.main.login.txtUsername

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val btnSignup : Button = findViewById(R.id.btnSignup)
        val btnSignin : Button = findViewById(R.id.btnSignin)
        val btnReset : Button = findViewById(R.id.btnReset)

        btnReset.setOnClickListener{
            txtUsername.setText("")
            txtPassword.setText("")
        }

        btnSignin.setOnClickListener{
            validateLogin()
        }

        btnSignup.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun validateLogin(){
        if(TextUtils.isEmpty(txtUsername.text)){
            txtUsername.setError(getString(R.string.empty_username))
        }
        else{
            txtUsername.setError(null)
        }
        if(TextUtils.isEmpty(txtPassword.text)){
            txtPassword.setError(getString(R.string.empty_password))
        }
        else{
            txtPassword.setError(null)
        }
        val loginUser = txtUsername.text.toString()
        val loginPassword = txtPassword.text.toString()
        if(loginUser.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please fill in the name.",
                Toast.LENGTH_LONG
            ).show()
        }
        else if(loginPassword.isEmpty()){
            Toast.makeText(
                applicationContext,
                "Please fill in the password.",
                Toast.LENGTH_LONG
            ).show()
        }
        else{
            validateUser(User(loginUser,loginPassword,""))
        }
    }
    private fun validateUser(user: User) {
        val url = getString(R.string.url_server) + getString(R.string.url_user_login) + "?name=" + user.name + "&password=" + user.password
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()
                        val id:String =response.get("id").toString()
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("loggedId",id)
                        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
                        val editor=sharedPreferences.edit()
                        editor.putString("userID",id)
                        editor.commit()

                        startActivity(intent)
                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))

                }
            },
            Response.ErrorListener { error ->
                Log.d("Main", "Response: %s".format(error.message.toString())).toString()
                Toast.makeText(applicationContext, "Login Unsuccessful", Toast.LENGTH_LONG).show()
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
