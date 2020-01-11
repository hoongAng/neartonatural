package com.example.neartonatural.ui.posting

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.neartonatural.R

class PostingFragment : Fragment() {

    private lateinit var postingViewModel: PostingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postingViewModel =
            ViewModelProviders.of(this).get(PostingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_posting, container, false)
        val textView: TextView = root.findViewById(R.id.text_posting)
        postingViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }


}