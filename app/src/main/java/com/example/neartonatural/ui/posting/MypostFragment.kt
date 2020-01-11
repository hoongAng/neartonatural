package com.example.neartonatural.ui.posting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.neartonatural.R

class MypostFragment : Fragment() {

    private lateinit var mypostViewModel: MypostViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mypostViewModel =
            ViewModelProviders.of(this).get(MypostViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mypost, container, false)
        val textView: TextView = root.findViewById(R.id.text_mypost)
        mypostViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}