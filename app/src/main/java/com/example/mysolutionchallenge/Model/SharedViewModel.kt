package com.example.mysolutionchallenge.Model

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    //추가된 카테고리 data 받기
    private val liveData = MutableLiveData<ArrayList<String>>()

    //선택된 카테고리 데이터 받기 pilldata랑 같이
    private val categoryLiveData = MutableLiveData<HashMap<String, ArrayList<PillData>>>()
    fun getLiveData(): LiveData<ArrayList<String>> {
        return liveData
    }

    fun setLiveData(arr: ArrayList<String>) {
        liveData.value = arr
    }

    fun getCategoryLiveData(): LiveData<HashMap<String, ArrayList<PillData>>> {
        return categoryLiveData
    }

    fun setCategoryLiveData(arr: HashMap<String, ArrayList<PillData>>) {
        categoryLiveData.value = arr
    }
}