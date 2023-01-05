package com.planet.single.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.planet.core.app.getAppViewModel
import com.planet.single.ui.vm.MainVm
import com.planet.single.R
import com.planet.utils.ToastUtils

class TestActivity : AppCompatActivity() {


    private val mainVm by lazy { getAppViewModel<MainVm>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_test)

        mainVm.data.observe(this){
            ToastUtils.showLong(it)
        }

    }
}