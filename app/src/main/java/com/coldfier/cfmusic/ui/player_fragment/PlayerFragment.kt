package com.coldfier.cfmusic.ui.player_fragment

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import android.view.View
import android.widget.CheckBox
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.coldfier.cfmusic.App
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.FragmentPlayerBinding
import com.coldfier.cfmusic.media_browser_service.PlayerMediaBrowserService
import com.coldfier.cfmusic.ui.MainActivity
import com.coldfier.cfmusic.ui.base.BaseFragment
import com.coldfier.cfmusic.ui.picked_folder_fragment.ACTION_SONG_PICKED
import com.coldfier.cfmusic.ui.picked_folder_fragment.SONG_KEY
import com.coldfier.cfmusic.ui.player_fragment.PlayerWorker.Companion.ACTION_CHANGE_PLAY_BUTTON_ICON
import com.coldfier.cfmusic.ui.player_fragment.PlayerWorker.Companion.ACTION_UPDATE_SONG_INFO
import com.coldfier.cfmusic.ui.player_fragment.PlayerWorker.Companion.EXTRA_IS_PLAYING
import com.coldfier.cfmusic.use_case.model.Song
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask
import kotlin.math.roundToLong

const val BOTTOM_SHEET_STATE = "com.coldfier.cfmusic.ui.player_fragment.bottom_sheet_state"

class PlayerFragment: BaseFragment<PlayerViewModel, FragmentPlayerBinding>(R.layout.fragment_player) {

    override val viewModel by viewModels<PlayerViewModel> { viewModelFactory }

    private val behavior by lazy {
        BottomSheetBehavior.from((requireActivity() as MainActivity).binding.bottomSheet)
    }

    @Inject
    lateinit var player: ExoPlayer

    private lateinit var mediaBrowser: MediaBrowserCompat

    private val connectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {

            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(requireContext(), token)
                MediaControllerCompat.setMediaController(requireActivity(), mediaController)
            }

            buildTransportControls()
        }

        override fun onConnectionSuspended() {
            //TODO - The Service has crashed. Disable transport controls until it automatically reconnects
        }

        override fun onConnectionFailed() {
            //TODO - The Service has refused our connection
        }
    }

    private val controllerCallback = object : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
        }

        override fun onSessionDestroyed() {
            mediaBrowser.disconnect()
        }
    }

    private val songBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ACTION_SONG_PICKED, ACTION_UPDATE_SONG_INFO -> {
                    val song = intent.getParcelableExtra<Song>(SONG_KEY)
                    song?.let { viewModel.setCurrentSong(it) }
                }

                ACTION_CHANGE_PLAY_BUTTON_ICON -> {
                    val isPlaying = intent.getBooleanExtra(EXTRA_IS_PLAYING, false)
                    binding.btnPlaySong.isChecked = isPlaying
                    binding.btnPlaySongCollapsed.isChecked = isPlaying
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireContext().applicationContext as App).appComponent.injectPlayer(this)

        mediaBrowser = MediaBrowserCompat(
            requireContext(),
            ComponentName(requireContext(), PlayerMediaBrowserService::class.java),
            connectionCallback,
            null
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initClickers()
        initObservers()
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

    override fun onStart() {
        super.onStart()

        startTimer()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            songBroadcastReceiver,
            IntentFilter(ACTION_SONG_PICKED).apply {
                addAction(ACTION_UPDATE_SONG_INFO)
                addAction(ACTION_CHANGE_PLAY_BUTTON_ICON)
            }
        )

        mediaBrowser.connect()
    }

    override fun onStop() {
        super.onStop()

        stopTimer()
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(songBroadcastReceiver)

        MediaControllerCompat
            .getMediaController(requireActivity())?.unregisterCallback(controllerCallback)

        mediaBrowser.disconnect()
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
                player.let {
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
                player.let {
                    val pickedPosition = slider.value
                    val audioDuration = it.duration

                    val newPosition = pickedPosition * audioDuration / 100

                    it.seekTo(newPosition.roundToLong())
                }
            }
        })

//        binding.btnPreviousSong.setOnClickListener {
//            val previousSongIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
//            previousSongIntent.putExtra(
//                Intent.EXTRA_KEY_EVENT,
//                KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
//            )
//            requireActivity().sendBroadcast(previousSongIntent)
//        }
//
//        binding.btnPreviousSongCollapsed.setOnClickListener {
//            val previousSongIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
//            previousSongIntent.putExtra(
//                Intent.EXTRA_KEY_EVENT,
//                KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
//            )
//            requireActivity().sendBroadcast(previousSongIntent)
//        }
//
//        binding.btnPlaySong.setOnClickListener {
//            if (!(it as CheckBox).isChecked) {
//                pause()
//            } else {
//                play()
//            }
//        }
//
//        binding.btnPlaySongCollapsed.setOnClickListener {
//            if (!(it as CheckBox).isChecked) {
//                pause()
//            } else {
//                play()
//            }
//        }
//
//        binding.btnNextSong.setOnClickListener {
//            val nextSongIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
//            nextSongIntent.putExtra(
//                Intent.EXTRA_KEY_EVENT,
//                KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT)
//            )
//            requireActivity().sendBroadcast(nextSongIntent)
//        }
//
//        binding.btnNextSongCollapsed.setOnClickListener {
//            val nextSongIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
//            nextSongIntent.putExtra(
//                Intent.EXTRA_KEY_EVENT,
//                KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT)
//            )
//            requireActivity().sendBroadcast(nextSongIntent)
//        }
    }

    private fun initObservers() {

        collectFlowInCoroutine {
            viewModel.currentSongStateFlow.collect { song ->
                renderSongInfo(song)
            }
        }
    }

    private var timerTask: TimerTask? = null
    private var timer = Timer()

    private fun play() {
        val playPauseIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        playPauseIntent.putExtra(
            Intent.EXTRA_KEY_EVENT,
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
        )
        requireActivity().sendBroadcast(playPauseIntent)

        startTimer()
    }

    private fun pause() {
        val playPauseIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        playPauseIntent.putExtra(
            Intent.EXTRA_KEY_EVENT,
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
        )
        requireActivity().sendBroadcast(playPauseIntent)

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
                    player.let {
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

    private fun buildTransportControls() {
        val mediaController = MediaControllerCompat.getMediaController(requireActivity())

        binding.btnPreviousSong.setOnClickListener {
            mediaController.transportControls.skipToPrevious()
        }

        binding.btnPreviousSongCollapsed.setOnClickListener {
            mediaController.transportControls.skipToPrevious()
        }

        binding.btnPlaySong.setOnClickListener {
            if (!(it as CheckBox).isChecked) {
                mediaController.transportControls.pause()
                pause()
            } else {
                mediaController.transportControls.play()
                play()
            }
        }

        binding.btnPlaySongCollapsed.setOnClickListener {
            if (!(it as CheckBox).isChecked) {
                mediaController.transportControls.pause()
                pause()
            } else {
                mediaController.transportControls.play()
                play()
            }
        }

        binding.btnNextSong.setOnClickListener {
            mediaController.transportControls.skipToNext()
        }

        binding.btnNextSongCollapsed.setOnClickListener {
            mediaController.transportControls.skipToNext()
        }

        val metadata = mediaController.metadata
        val pbState = mediaController.playbackState
        mediaController.registerCallback(controllerCallback)
    }
}