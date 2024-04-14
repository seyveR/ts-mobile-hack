package com.example.hakaton.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _camera2Text = MutableLiveData<String>()
    val camera2Text: LiveData<String> = _camera2Text

    val imageUploadResult = MutableLiveData<Boolean>()

    private val _opisanieText = MutableLiveData<String>()
    val opisanieText: LiveData<String> = _opisanieText

    private val _camera3Text = MutableLiveData<String>()
    val camera3Text: LiveData<String> = _camera3Text

    fun setCamera2Text(text: String) {
        _camera2Text.value = text
    }

    fun setOpisanieText(text: String) {
        _opisanieText.value = text
    }

    fun setCamera3Text(text: String) {
        _camera3Text.value = text
    }
}

