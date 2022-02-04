package com.coldfier.cfmusic.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.coldfier.cfmusic.App
import com.coldfier.cfmusic.di.factory.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<VM: BaseViewModel, MBinding: ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
): Fragment() {

    private var _binding: MBinding? = null
    val binding: MBinding
        get() = _binding!!

    abstract val viewModel: VM

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireContext().applicationContext as App).appComponent.inject(this as BaseFragment<BaseViewModel, ViewDataBinding>)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun collectFlowInCoroutine(func: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                func.invoke()
            }
        }
    }
}