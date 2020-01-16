package com.example.neartonatural.ui.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neartonatural.R
import com.example.neartonatural.ui.home.HomeActivity
import com.example.neartonatural.ui.home.MySingleton
import kotlinx.android.synthetic.main.activity_help.*
import org.json.JSONArray
import org.json.JSONObject

class HelpActivity: AppCompatActivity()  {

        lateinit var helpList: ArrayList<Help>
        lateinit var adapter: HelpListAdapter
        var id:String=""
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
            val id =sharedPreferences.getString("userID","")
            //Initialise variables and UI
            helpList = ArrayList()
            setContentView(R.layout.activity_help)
            adapter = HelpListAdapter(this,id)
            adapter.setHelp(helpList)
            this.id=id
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_help)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            val btnPost : Button = findViewById(R.id.btnPost)
            showHelp()
            btnPost.setOnClickListener{
                post()
            }

        }

        private fun showHelp() {
            val url = getString(R.string.url_server) + getString(R.string.url_read_help)

            helpList.clear()
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    // Process the JSON
                    try {
                        if (response != null) {
                            val strResponse = response.toString()
                            val jsonResponse = JSONObject(strResponse)
                            val jsonArray: JSONArray = jsonResponse.getJSONArray("records")

                            val size: Int = jsonArray.length()

                            for (i in 0..size - 1) {
                                var jsonUser: JSONObject = jsonArray.getJSONObject(i)
                                var help: Help = Help(
                                    jsonUser.getString("question"),
                                    jsonUser.getString("reply"),
                                    jsonUser.getInt("id")
                                )
                                helpList.add(help)
                            }

                            // progress.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        Log.d("Main", "Response: %s".format(e.message.toString()))
                        //progress.visibility = View.GONE

                    }
                },
                Response.ErrorListener { error ->
                    Log.d("Main", "Response: %s".format(error.message.toString()))
                    //progress.visibility = View.GONE
                }
            )

            //Volley request policy, only one time request
            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                0, //no retry
                1f
            )

            // Access the RequestQueue through your singleton class.
            jsonObjectRequest.tag = HelpActivity.TAG
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }
        fun post(){
            val url =
                this.getString(R.string.url_server) + this.getString(R.string.url_create_help) + "?question="+ questioning.text.toString()+"&reply=PENDING REPLY"+ "&id="+id
            println(url)
            val jsonObjectRequest = JsonObjectRequest(

                Request.Method.GET, url, null,
                Response.Listener { response ->
                    // Process the JSON
                    try {
                        if (response != null) {
                            val success: String = response.get("success").toString()
                            if (success.equals("1")) {
                                Toast.makeText(this, "Question Added.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, this::class.java)
                                startActivity(intent)
                                //Add record to user list
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
            com.example.neartonatural.ui.account.MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


        }
        companion object {
            const val TAG = "com.example.neartonatural"
        }


}