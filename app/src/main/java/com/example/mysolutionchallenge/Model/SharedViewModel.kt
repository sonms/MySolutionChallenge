package com.example.mysolutionchallenge.Model

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    private val liveData = MutableLiveData<ArrayList<String>>()
    fun getLiveData(): LiveData<ArrayList<String>> {
        return liveData
    }

    fun setLiveData(arr: ArrayList<String>) {
        liveData.value = arr
    }
}