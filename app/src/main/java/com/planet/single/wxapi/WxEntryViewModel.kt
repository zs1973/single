package com.planet.single.wxapi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planet.network.util.Status
import com.planet.single.repo.AppDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *作者：张硕
 *日期：2022/04/07
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：
 **/
@HiltViewModel
class WxEntryViewModel @Inject constructor(
    private val mDataRepository: AppDataRepository,
) : ViewModel() {

    private val _loginStatus = MutableLiveData<Status>()
    val loginStatus = _loginStatus

    private val _toast = MutableLiveData<String>()
    val toast = _toast

    /**
     * 登录时间守护服服务器
     * @param code String
     */
    fun loginTimeKeeperServer(code: String) {
        viewModelScope.launch {
            _loginStatus.value = Status.Loading
            // val resource = mDataRepository.login(code)
            // when (resource.status) {
            //     Status.Error -> {
            //         resource.message?.let {
            //             _toast.value = it
            //         }
            //     }
            //     Status.Loading -> {}
            //     Status.Success -> {
            //
            //     }
            // }
            // _loginStatus.value = resource.status
        }
    }
}