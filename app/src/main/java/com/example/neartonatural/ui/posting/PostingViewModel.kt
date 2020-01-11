package com.example.neartonatural.ui.posting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is posting Fragment"
    }
    val text: LiveData<String> = _text
}