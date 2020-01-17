package com.example.neartonatural.ui.posting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.example.neartonatural.ui.home.PostListAdapter
import org.json.JSONArray
import org.json.JSONObject

class HidedActivity : AppCompatActivity() {
    lateinit var hideList: ArrayList<Hide>
    lateinit var adapter: HidedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val id =sharedPreferences.getString("userID","")
        //Initialise variables and UI
        hideList = ArrayList()
        setContentView(R.layout.activity_hide)
        adapter = HidedListAdapter(this,id)
        adapter.setHide(hideList)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_hide)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        showHide(id)

        val clearAllHide:Button=findViewById(R.id.button2)
        clearAllHide.setOnClickListener{
            clearAllHided(id)
            intent = Intent(this,this::class.java)
            startActivity(intent)
        }
    }

    private fun showHide(id:String) {
        val url = getString(R.string.url_server) + getString(R.string.url_hide_read) + "?id="+id

        hideList.clear()
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
                            var hide: Hide = Hide(
                                jsonUser.getString("name"),
                                jsonUser.getString("postId"),
                                jsonUser.getString("postDesc")
                            )
                            hideList.add(hide)
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
        jsonObjectRequest.tag = HidedActivity.TAG
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    private fun clearAllHided(id:String){
        val url = getString(R.string.url_server) + getString(R.string.url_hide_deleteAll) + "?id="+id

        hideList.clear()
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                        try {
                            if (response != null) {
                                val success: String = response.get("success").toString()
                                if (success.equals("1")) {
                                    Toast.makeText(this, "Successful Removed All record from Hide Post", Toast.LENGTH_SHORT).show()
                                    //Add record to user list
                                }
                                else
                                {
                                    Toast.makeText(this, "There is no record found!!!!", Toast.LENGTH_SHORT).show()
                                }
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
        jsonObjectRequest.tag = FavouriteActivity.TAG
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    companion object {
        const val TAG = "com.example.neartonatural"
    }

}