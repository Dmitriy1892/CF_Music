package com.coldfier.cfmusic.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.ActivityMainBinding
import com.coldfier.cfmusic.ui.base.BaseActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity: BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main) {

    override val viewModel: () -> MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            requestReadExternalStoragePermission()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher.launch("audio/*")

        if (!checkReadExternalStoragePermission()) {
            requestReadExternalStoragePermission()
        }
    }

    private fun checkReadExternalStoragePermission(): Boolean {
        val permissionRead = ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE
        )

        return  permissionRead == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            1
        )
    }
}