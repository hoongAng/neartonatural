package com.example.neartonatural.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neartonatural.R
import com.example.neartonatural.ui.account.MySingleton

class PostListAdapter internal constructor(context: Context, id: String) :
    RecyclerView.Adapter<PostListAdapter.UserViewHolder>() {
    private val context1 = context
    private val id = id
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var posts = emptyList<Post>() // Cached copy of user
    private var postID:Int=0
    private var likeCount:Int=0

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.userName_post)
        val postDescription: TextView = itemView.findViewById(R.id.postContent)
        val likeCountView: TextView = itemView.findViewById(R.id.likeCount)
        val createdAtView: TextView = itemView.findViewById(R.id.postedAt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = inflater.inflate(R.layout.post_record, parent, false)
        val like: ImageButton = itemView.findViewById((R.id.likeBtn))
        val unlike: ImageButton = itemView.findViewById((R.id.unlikeBtn))
        val fav: ImageButton = itemView.findViewById((R.id.favBtn))
        val unfav: ImageButton = itemView.findViewById((R.id.unfavBtn))
        val hide: Button =itemView.findViewById(R.id.btnUnHide)
        val likes:TextView=itemView.findViewById(R.id.likeCount)
        hide.setOnClickListener{
            itemView.visibility=View.GONE
            hided()
        }
        setButtonFunc(like, unlike, fav, unfav,likes)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = posts[position]
        postID = current.postId.toInt()
        likeCount = current.like
        holder.userNameTextView.text = current.name
        holder.postDescription.text = current.postDesc
        holder.likeCountView.text = current.like.toString()
        holder.createdAtView.text = "Created At : " + current.created_at
    }

    internal fun setPost(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    override fun getItemCount() = posts.size

    private fun setButtonFunc(
        like: ImageButton,
        unlike: ImageButton,
        fav: ImageButton,
        unfav: ImageButton,
        txtLike:TextView
    ) {
        like.visibility = View.GONE
        fav.visibility = View.GONE
        unfav.setOnClickListener {
            unfav.visibility = View.GONE
            fav.visibility = View.VISIBLE
            addFav()
        }
        fav.setOnClickListener {
            unfav.visibility = View.VISIBLE
            fav.visibility = View.GONE
            removeFac()
        }
        like.setOnClickListener {
            unlike.visibility = View.VISIBLE
            like.visibility = View.GONE
            decreaseLike()
            txtLike.setText(likeCount.toString())
        }
        unlike.setOnClickListener {
            unlike.visibility = View.GONE
            like.visibility = View.VISIBLE
            increaseLike()
            txtLike.setText(likeCount.toString())
        }
    }

    private fun addFav() {
        val url =
            context1.getString(R.string.url_server) + context1.getString(R.string.url_favourite_create) + "?id=" + id + "&postId="+postID
        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()
                        if (success.equals("1")) {
                            Toast.makeText(context1, "Added to Favourite!!!", Toast.LENGTH_SHORT).show()
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
        MySingleton.getInstance(context1).addToRequestQueue(jsonObjectRequest)
    }

    private fun removeFac() {
        val url =
            context1.getString(R.string.url_server) + context1.getString(R.string.url_favourite_delete) + "?id=" + id + "&postId="+postID
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()
                        if (success.equals("1")) {
                            Toast.makeText(context1, "Removed Favourite!!!", Toast.LENGTH_SHORT).show()
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
        MySingleton.getInstance(context1).addToRequestQueue(jsonObjectRequest)

    }

    private fun hided() {
        val url =
            context1.getString(R.string.url_server) + context1.getString(R.string.url_hide_create) + "?id=" + id + "&postId="+postID
        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()
                        if (success.equals("1")) {
                            Toast.makeText(context1, "Hided!!!", Toast.LENGTH_SHORT).show()
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
        MySingleton.getInstance(context1).addToRequestQueue(jsonObjectRequest)
    }
    private fun increaseLike(){
        likeCount+=1

        val url =
            context1.getString(R.string.url_server) + context1.getString(R.string.url_like_inc) + "?likes=" + likeCount + "&postId="+postID
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()
                        if (success.equals("1")) {
                            Toast.makeText(context1, "Liked!!!", Toast.LENGTH_SHORT).show()
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
        MySingleton.getInstance(context1).addToRequestQueue(jsonObjectRequest)


    }
    private fun decreaseLike(){
        likeCount-=1

        val url =
            context1.getString(R.string.url_server) + context1.getString(R.string.url_like_inc) + "?likes=" + likeCount + "&postId="+postID
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()
                        if (success.equals("1")) {
                            Toast.makeText(context1, "Unliked!!!", Toast.LENGTH_SHORT).show()
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
        MySingleton.getInstance(context1).addToRequestQueue(jsonObjectRequest)

    }
}
