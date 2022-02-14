package com.coldfier.cfmusic.ui.player_fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.FragmentPlayerBinding
import com.coldfier.cfmusic.ui.MainActivity
import com.coldfier.cfmusic.ui.base.BaseFragment
import com.coldfier.cfmusic.ui.picked_folder_fragment.ACTION_SONG_PICKED
import com.coldfier.cfmusic.ui.picked_folder_fragment.SONG_KEY
import com.coldfier.cfmusic.use_case.model.Song
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.roundToLong

const val BOTTOM_SHEET_STATE = "com.coldfier.cfmusic.ui.player_fragment.bottom_sheet_state"

class PlayerFragment: BaseFragment<PlayerViewModel, FragmentPlayerBinding>(R.layout.fragment_player) {

    override val viewModel by viewModels<PlayerViewModel> { viewModelFactory }

    private val behavior by lazy {
        BottomSheetBehavior.from((requireActivity() as MainActivity).binding.bottomSheet)
    }

    private var player: ExoPlayer? = null

    val songBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_SONG_PICKED) {
                val song = intent.getParcelableExtra<Song>(SONG_KEY)
                song?.let { viewModel.setCurrentSong(it) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initClickers()
        initObservers()

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(songBroadcastReceiver, IntentFilter(
            ACTION_SONG_PICKED)
        )
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

    override fun onStop() {
        super.onStop()
        stopTimer()
    }

    override fun onStart() {
        super.onStart()

        if (player != null) {
            startTimer()
        }
    }

    private fun initViews() {
        binding.vpSongPreviews.adapter = PlayerViewPagerAdapter()
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
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                player?.let {
                    val pickedPosition = slider.value.toLong()
                    val audioDuration = it.duration

                    val newPosition = pickedPosition * audioDuration / 100

                    it.seekTo(newPosition)
                }
            }
        })

        binding.sliderTrackCollapsed.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {

            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                player?.let {
                    val pickedPosition = slider.value
                    val audioDuration = it.duration

                    val newPosition = pickedPosition * audioDuration / 100

                    it.seekTo(newPosition.roundToLong())
                }
            }
        })

        binding.btnPreviousSong.setOnClickListener {

        }

        binding.btnPlaySong.setOnClickListener {
            if (!(it as CheckBox).isChecked) {
                pause()
            } else {
                play()
            }
        }

        binding.btnPlaySongCollapsed.setOnClickListener {
            if (!(it as CheckBox).isChecked) {
                pause()
            } else {
                play()
            }
        }

        binding.btnNextSong.setOnClickListener {

        }
    }

    private fun initObservers() {

        collectFlowInCoroutine {
            viewModel.currentSongStateFlow.collect { song ->
                song.fullPath?.let { path ->
                    preparePlayer(path)
                }
                renderSongInfo(song)
            }
        }
    }

    private var timerTask: TimerTask? = null
    private var timer = Timer()

    private fun preparePlayer(filePath: String) {
        player = ExoPlayer.Builder(requireContext()).build()
        val uri = Uri.parse(filePath)
        val mediaItem = MediaItem.fromUri(uri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
    }

    private fun destroyPlayer() {
        player?.release()
        stopTimer()
    }

    private fun play() {
        player?.play()
        startTimer()
    }

    private fun pause() {
        player?.pause()
        stopTimer()
    }

    private fun renderSongInfo(song: Song) {
        (binding.vpSongPreviews.adapter as PlayerViewPagerAdapter).submitList(listOf(song))
        song.imageThumbnail?.let {
            binding.imageViewSongCollapsed.setImageBitmap(it)
        }

        song.songName?.let {
            binding.tvSongName.text = it
            binding.tvSongNameCollapsed.text = it
        }

        song.artist?.let {
            binding.tvSongArtist.text = it
            binding.tvSongArtistCollapsed.text = it
        }
    }

    private fun startTimer() {
        timerTask = timerTask {
            try {
                requireActivity().runOnUiThread {
                    player?.let {
                        val percentage = it.currentPosition * 100 / it.duration
                        binding.sliderTrack.value = percentage.toFloat()
                        binding.sliderTrackCollapsed.value = percentage.toFloat()
                    }
                }
            } catch (e: Exception) {

            }
        }
        timer = Timer()
        timer.schedule(timerTask, 0, 1000)
    }

    private fun stopTimer() {
        try {
            timer.cancel()
        } catch (e: Exception) {

        }
    }
}