package com.example.neartonatural.ui.home


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neartonatural.R
import com.example.neartonatural.ui.account.UpdateAccount
import com.example.neartonatural.ui.help.HelpActivity
import com.example.neartonatural.ui.posting.Favourite
import com.example.neartonatural.ui.posting.FavouriteActivity
import com.example.neartonatural.ui.posting.HidedActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.add_post.*
import org.json.JSONArray
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    lateinit var postList: ArrayList<Post>
    lateinit var adapter: PostListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val id =sharedPreferences.getString("userID","")

        //Initialise variables and UI
        postList = ArrayList()
        setContentView(R.layout.activity_home)
        adapter = PostListAdapter(this,id)
        adapter.setPost(postList)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_home)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        syncContact()

        btnAdd.setOnClickListener{
            intent = Intent(this, AddPost::class.java)
            startActivity(intent)
        }
        btnFav.setOnClickListener{
            intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        }
        btnHide.setOnClickListener{
            intent = Intent(this, HidedActivity::class.java)
            startActivity(intent)
        }
        btnHelp.setOnClickListener{
            intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
        btnProfile.setOnClickListener{
            intent = Intent(this, UpdateAccount::class.java)
            startActivity(intent)
        }
    }

    private fun syncContact() {
        val url = getString(R.string.url_server) + getString(R.string.url_post_read)

        postList.clear()

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
                            var post: Post = Post(
                                jsonUser.getString("name"),
                                jsonUser.getString("postId"),
                                jsonUser.getString("postDesc"),
                                jsonUser.getInt("likes"),
                                jsonUser.getString("created_at")
                            )
                            postList.add(post)
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
        jsonObjectRequest.tag = TAG
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onBackPressed() {
        MySingleton.getInstance(this).cancelRequest(TAG)
        super.onBackPressed()
    }

    private fun isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    companion object {
        const val TAG = "com.example.neartonatural"
    }
}
