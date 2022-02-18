package com.coldfier.cfmusic.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.coldfier.cfmusic.App
import com.coldfier.cfmusic.di.factory.ViewModelFactory
import javax.inject.Inject

abstract class BaseActivity<VM: BaseViewModel, MBinding: ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
) : AppCompatActivity() {

    private var _binding: MBinding? = null
    val binding: MBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as App).appComponent
            .injectBaseActivity(this as BaseActivity<BaseViewModel, ViewDataBinding>)

        _binding = DataBindingUtil.setContentView(this, layoutRes)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}