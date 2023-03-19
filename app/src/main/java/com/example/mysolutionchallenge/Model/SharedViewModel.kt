package com.example.mysolutionchallenge.Model

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    private val liveData = MutableLiveData<String>()
    fun getLiveData(): LiveData<String> {
        return liveData
    }

    fun setLiveData(str: String) {
        liveData.value = str
    }
}