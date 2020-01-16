package com.example.neartonatural.ui.posting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.neartonatural.R
import com.example.neartonatural.ui.account.MySingleton

class FavListAdapter internal constructor(context: Context, id: String) :
    RecyclerView.Adapter<FavListAdapter.UserViewHolder>() {
    private val context1 = context
    private val id = id
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var favourite = emptyList<Favourite>() // Cached copy of user
    private var postid =""

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.txtFavName)
        val shortDescription: TextView = itemView.findViewById(R.id.txtFavDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = inflater.inflate(R.layout.fav_record, parent, false)
        val unfav: ImageView = itemView.findViewById(R.id.favPageStar )

        unfav.setOnClickListener {
            itemView.visibility = View.GONE
            unfav()
        }
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = favourite[position]
        postid = current.postId
        holder.userNameTextView.text = current.name
        holder.shortDescription.text = current.postDesc
    }

    internal fun setFavourite(favourite: List<Favourite>) {
        this.favourite=favourite
        notifyDataSetChanged()
    }

    override fun getItemCount() = favourite.size

    private fun unfav(){
        val url =
            context1.getString(R.string.url_server) + context1.getString(R.string.url_favourite_delete) + "?id=" + id + "&postId="+postid
        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()
                        if (success.equals("1")) {
                            Toast.makeText(context1, "Successful Removed from Favourite Post", Toast.LENGTH_SHORT).show()
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