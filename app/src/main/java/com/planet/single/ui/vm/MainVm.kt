package com.planet.single.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVm @Inject constructor(): ViewModel() {


    val data = MutableLiveData<String>()

    fun send(){
        data.value = "sdfsdfsdf"
    }

}