package com.example.neartonatural.ui.account

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
    lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_account)
        val btnButton=findViewById<Button>(R.id.btnRegister)
        val editUsername=findViewById<EditText>(R.id.txtUsername)
        val editContact=findViewById<EditText>(R.id.editTextContact)
        val editPassword=findViewById<EditText>(R.id.txtPassword)
        val editConPassword=findViewById<EditText>(R.id.txtConPassword)
        setTitle("Registration")

        btnButton.setOnClickListener {
            val user=saveUser()
            if(user.name!=null&&user.contact!=null&&user.password!=null){
                createUser(saveUser())
            }

        }
    }
    private fun createUser(user: User) {
        val url = getString(R.string.url_server) + getString(R.string.url_user_create) + "?name=" + user.name +
                "&contact=" + user.contact+"&password=" + user.password

        progress.visibility = View.GONE

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val success: String = response.get("success").toString()

                        if(success.equals("1")){
                            Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext, "Record not saved", Toast.LENGTH_LONG).show()
                        }
                        progress.visibility = View.GONE
                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))
                    progress.visibility = View.GONE

                }
            },
            Response.ErrorListener { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))
                progress.visibility = View.GONE
            }
        )

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )


    }
    private fun saveUser(): User {
        val name = txtUsername.text.toString()
        val contact = editTextContact.text.toString()
        val password = txtPassword.text.toString()

        val user = User(name, password, contact)

        if(TextUtils.isEmpty(txtUsername.text)){
            txtUsername.setError(getString(R.string.error_value_required))
            return user
        }
        if(TextUtils.isEmpty(editTextContact.text)){
            editTextContact.setError(getString(R.string.error_value_required))
            return user
        }
        if(TextUtils.isEmpty(txtPassword.text)){
            txtPassword.setError(getString(R.string.error_value_required))
            return user
        }

        return user
    }

    companion object{
        const val TAG = "com.example.neartonatural"
    }
}

