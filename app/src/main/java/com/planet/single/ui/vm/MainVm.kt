package com.planet.single.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planet.single.repo.AppDataRepository
import com.planet.utils.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVm @Inject constructor(
    private val mAdp :AppDataRepository
): ViewModel() {

    val data = MutableLiveData<String>()

    fun send(){
        data.value = "sdfsdfsdf"
    }

    fun getKind(){
       viewModelScope.launch {
          val response = mAdp.getKind()
           response.status
           LogUtils.loge("xxxx",response.data.toString())
       }
    }

}