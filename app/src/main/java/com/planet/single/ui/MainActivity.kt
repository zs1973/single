package com.planet.single.ui

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import com.planet.core.ui.ImmersionActivity
import com.planet.core.ui.dialog.SimpleDialogFragment
import com.planet.core.ui.dialog.TextDialogFragment
import com.planet.single.R
import com.planet.single.databinding.ActivityMainBinding
import com.planet.single.ui.vm.MainVm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ImmersionActivity<ActivityMainBinding>() {

    private val mVm: MainVm by viewModels()

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData(savedInstanceState: Bundle?) {}

    override fun listeners() {
        findViewById<Button>(R.id.btn).setOnClickListener {
            mVm.getKind()

            TextDialogFragment.build {
                title = "标题"
                content = "内容"
                cancelText = "取消"
                confirmText = "确定"
                hideCancelBtn = true
                confirmClickListener = object : SimpleDialogFragment.OnConfirmClickListener {
                    override fun onConfirm(dialogFragment: SimpleDialogFragment) {
                        dialogFragment.dismiss()
                    }
                }
            }.show(supportFragmentManager, null)
            //startActivity(Intent(this, TestActivity::class.java))
        }
    }

    override fun doBusiness() {}

    override fun observerData() {}

    override fun fitWindow(): Boolean = true

    override fun colorRes(): Int = com.planet.core.R.color.pla_status_bar_color
}