package com.example.neartonatural.ui.help

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

class HelpListAdapter internal constructor(context: Context, id: String) :
    RecyclerView.Adapter<HelpListAdapter.UserViewHolder>(){
        private val id = id
        private val inflater: LayoutInflater = LayoutInflater.from(context)
        private var help = emptyList<Help>() // Cached copy of user


        inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val userQuestionTextView: TextView = itemView.findViewById(R.id.txtQuestion)
            val devReplyTextView: TextView = itemView.findViewById(R.id.txtReply)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val itemView = inflater.inflate(R.layout.help_record, parent, false)
            return UserViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val current = help[position]
            holder.userQuestionTextView.text = current.question
            holder.devReplyTextView.text = current.reply
        }

        internal fun setHelp(help: List<Help>) {
            this.help=help
            notifyDataSetChanged()
        }

        override fun getItemCount() = help.size
}