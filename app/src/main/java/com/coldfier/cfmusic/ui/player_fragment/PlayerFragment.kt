package com.coldfier.cfmusic.ui.player_fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.FragmentPlayerBinding
import com.coldfier.cfmusic.ui.MainActivity
import com.coldfier.cfmusic.ui.base.BaseFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val BOTTOM_SHEET_STATE = "com.coldfier.cfmusic.ui.player_fragment.bottom_sheet_state"

class PlayerFragment: BaseFragment<PlayerViewModel, FragmentPlayerBinding>(R.layout.fragment_player) {

    override val viewModel by viewModels<PlayerViewModel> { viewModelFactory }

    private val behavior by lazy {
        BottomSheetBehavior.from((requireActivity() as MainActivity).binding.bottomSheet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initClickers()
        initObservers()

        if (!checkExternalStoragePermissions()) {
            requestExternalStoragePermission()
            viewModel.getSongList()
        } else {
            viewModel.getSongList()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BOTTOM_SHEET_STATE, behavior.state)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getInt(BOTTOM_SHEET_STATE)?.let {
            if (it == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                with(binding) {
                    layoutCollapsed.visibility = View.GONE
                    layoutExpanded.visibility = View.VISIBLE
                    val activityAppBar = (requireActivity() as MainActivity).binding.appbar
                    activityAppBar.setExpanded(false, false)
                }
            }
        }
    }

    private fun initViews() {
        binding.vpSongPreviews.adapter = PlayerViewPagerAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(1000)
                Toast.makeText(requireContext(), viewModel.getHello(), Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun initClickers() {

        binding.btnCollapse.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.layoutCollapsed.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                with(binding) {
                    if (slideOffset > 0) {
                        layoutCollapsed.alpha = 1 - 2 * slideOffset
                        layoutExpanded.alpha = slideOffset * slideOffset

                        val activityAppBar = (requireActivity() as MainActivity).binding.appbar

                        if (slideOffset > 0.5) {
                            layoutCollapsed.visibility = View.GONE
                            layoutExpanded.visibility = View.VISIBLE
                            activityAppBar.setExpanded(false, false)
                        }

                        if (slideOffset < 0.5 && binding.layoutExpanded.visibility == View.VISIBLE) {
                            layoutCollapsed.visibility = View.VISIBLE
                            layoutExpanded.visibility = View.GONE
                            activityAppBar.setExpanded(true)
                        }
                    }
                }
            }
        })

        binding.cbFavorite.setOnCheckedChangeListener { _, isChecked ->

        }

        binding.cbVolume.setOnCheckedChangeListener { _, isChecked ->

        }

        binding.cbRepeat.setOnCheckedChangeListener { _, isChecked ->

        }

        binding.cbShuffle.setOnCheckedChangeListener { _, isChecked ->

        }

        binding.sliderTrack.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {

            }
        })

        binding.btnPreviousSong.setOnClickListener {

        }

        binding.btnPlaySong.setOnClickListener {

        }

        binding.btnNextSong.setOnClickListener {

        }
    }

    private fun initObservers() {
        collectFlowInCoroutine {
            viewModel.roomSongListStateFlow.collect {
                Toast.makeText(requireContext(), "Oooookaaaaaay, let's go....", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initPlayer() {
        val player = ExoPlayer.Builder(requireContext()).build()
//        val mediaItem = MediaItem.fromUri()
    }

    private fun checkExternalStoragePermissions(): Boolean {
        val permissionRead = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val permissionWrite = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        return  permissionRead == PackageManager.PERMISSION_GRANTED
                &&  permissionWrite == PackageManager.PERMISSION_GRANTED
    }

    private fun requestExternalStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            1
        )
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            viewModel.getSongList()
        } else {
            requestExternalStoragePermission()
        }
    }
}