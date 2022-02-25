package com.coldfier.cfmusic.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.ActivityMainBinding
import com.coldfier.cfmusic.ui.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class MainActivity: BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main) {

    override val viewModel: MainViewModel by viewModels{ viewModelFactory }

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            requestReadExternalStoragePermission()
        } else {
            viewModel.getAllSongs()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!checkReadExternalStoragePermission()) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestReadExternalStoragePermission()
        } else {
            viewModel.getAllSongs()
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.albumsFragment, R.id.foldersFragment))

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {
                    }

                    1 -> {
                    }

                    2 -> {
                    }

                    3 -> {
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
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
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }
}