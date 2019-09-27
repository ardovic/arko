package com.skosc.arko.app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private val sumator: Sumator) : ViewModel() {
    val counter: MutableLiveData<Int> = MutableLiveData<Int>().apply {
        value = 0
    }

    fun add(int: Int) {
        counter.value = sumator.sum(counter.value ?: 0, int)
    }
}