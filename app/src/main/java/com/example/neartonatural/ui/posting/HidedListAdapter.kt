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
import com.example.neartonatural.ui.home.Post

class HidedListAdapter internal constructor(context: Context, id: String) :
    RecyclerView.Adapter<HidedListAdapter.UserViewHolder>() {
    private val context1 = context
    private val id = id
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var hide = emptyList<Hide>() // Cached copy of user
    private var postid =""

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.userName_hide)
        val shortDescription: TextView = itemView.findViewById(R.id.Desc_hide)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = inflater.inflate(R.layout.hide_record, parent, false)
        val unhide: Button = itemView.findViewById(R.id.btnUnHide)
        unhide.setOnClickListener {
            itemView.visibility = View.GONE
            unhides()
        }
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = hide[position]
        postid = current.postId
        holder.userNameTextView.text = current.name
        holder.shortDescription.text = current.postDesc
    }

    internal fun setHide(hide: List<Hide>) {
        this.hide=hide
        notifyDataSetChanged()
    }

    override fun getItemCount() = hide.size

    private fun unhides(){
        val url =
            context1.getString(R.string.url_server) + context1.getString(R.string.url_hide_delete) + "?id=" + id + "&postId="+postid
        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try {
                    if (response != null) {
                        val success: String = response.get("success").toString()
                        if (success.equals("1")) {
                            Toast.makeText(context1, "Successful Removed from Hide Post", Toast.LENGTH_SHORT).show()
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