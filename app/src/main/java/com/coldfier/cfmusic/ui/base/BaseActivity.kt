package com.coldfier.cfmusic.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VM: BaseViewModel, MBinding: ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
) : AppCompatActivity() {

    private var _binding: MBinding? = null
    val binding: MBinding
        get() = _binding!!

    abstract val viewModel: () -> VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, layoutRes)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}