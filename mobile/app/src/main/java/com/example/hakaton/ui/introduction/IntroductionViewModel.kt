package com.example.hakaton.ui.introduction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IntroductionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Introduction Fragment"
    }
    val text: LiveData<String> = _text
}