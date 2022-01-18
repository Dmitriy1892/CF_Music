package com.coldfier.cfmusic.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<VM: BaseViewModel, MBinding: ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
): Fragment() {

    private var _binding: MBinding? = null
    val binding: MBinding
        get() = _binding!!

    abstract val viewModel: () -> VM

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
}