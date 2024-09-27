package com.route.todoapplication.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var _binding: VB? = null
    val binding: VB get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), null, false)
        setContentView(_binding?.root)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getLayoutId(): Int


}