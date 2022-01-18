package com.coldfier.cfmusic.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.ActivityMainBinding
import com.coldfier.cfmusic.ui.base.BaseActivity

class MainActivity: BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main) {

    override val viewModel: () -> MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}