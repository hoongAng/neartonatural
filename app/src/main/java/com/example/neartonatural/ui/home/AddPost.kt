package com.example.neartonatural.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neartonatural.R
import com.example.neartonatural.ui.account.MainActivity
import com.example.neartonatural.ui.account.MySingleton
import com.example.neartonatural.ui.account.User
import kotlinx.android.synthetic.main.add_post.*
import kotlinx.android.synthetic.main.fragment_account.*

class AddPost: AppCompatActivity()  {
    val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
    val id =sharedPreferences.getString("userID","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_post)

        addpost()

    }

    fun addpost(){
        if(TextUtils.isEmpty(addPostText.text)){
            addPostText.setError(getString(R.string.empty_username))
        }
        else{
            addPostText.setError(null)
            createPost(addPostText.toString(),id)
        }
    }
        private fun createPost(desc:String,id:String) {
            val url = getString(R.string.url_server) + getString(R.string.url_post_create) + "?postDesc=" + desc + "&postPic=null" + "&id="+id
            val jsonObjectRequest = JsonObjectRequest(

                Request.Method.GET, url, null,
                Response.Listener { response ->
                    // Process the JSON
                    try{
                        if(response != null){
                            val success: String = response.get("success").toString()

                            if(success.equals("1")){
                                Toast.makeText(applicationContext, "Posted Successful", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                //Add record to user list
                            }else{
                                Toast.makeText(applicationContext, "Post Unsuccessful", Toast.LENGTH_LONG).show()
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