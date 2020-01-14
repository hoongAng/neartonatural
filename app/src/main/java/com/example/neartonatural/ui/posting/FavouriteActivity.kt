package com.example.neartonatural.ui.posting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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

class FavouriteActivity : AppCompatActivity() {
    lateinit var favList: ArrayList<Favourite>
    lateinit var adapter: FavListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val id =sharedPreferences.getString("userID","")
        //Initialise variables and UI
        favList = ArrayList()
        setContentView(R.layout.activity_hide)
        adapter = FavListAdapter(this,id)
        adapter.setFavourite(favList)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_hide)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        showFavourite(id)

    }

    private fun showFavourite(id:String) {
        val url = getString(R.string.url_server) + getString(R.string.url_hide_read) + "?id="+id

        favList.clear()
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
                            var favourite: Favourite = Favourite(
                                jsonUser.getString("name"),
                                jsonUser.getString("postId"),
                                jsonUser.getString("postDesc")
                            )
                            favList.add(favourite)
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
        jsonObjectRequest.tag = FavouriteActivity.TAG
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    companion object {
        const val TAG = "com.example.neartonatural"
    }

}