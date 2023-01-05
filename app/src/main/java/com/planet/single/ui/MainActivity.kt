package com.planet.single.ui

import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.planet.core.app.getAppViewModel
import com.planet.core.ui.ImmersionActivity
import com.planet.single.ui.vm.MainVm
import com.planet.single.R
import com.planet.single.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ImmersionActivity<ActivityMainBinding>() {



    val mVm: MainVm by lazy { getAppViewModel() }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData(savedInstanceState: Bundle?) {

        lifecycleScope.launch{

        }
    }

    override fun listeners() {
        findViewById<Button>(R.id.btn).setOnClickListener {
            mVm.send()
//            startActivity(Intent(this, TestActivity::class.java))
        }
    }

    override fun doBusiness() {
    }

    override fun observerData() {
    }

    override fun fitWindow(): Boolean {
        return true
    }

    override fun colorRes(): Int {
        return R.color.white
    }
}