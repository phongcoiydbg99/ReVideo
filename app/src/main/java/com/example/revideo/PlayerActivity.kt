package com.example.revideo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import com.example.revideo.databinding.ActivityMainBinding
import com.example.revideo.databinding.ActivityPlayerBinding
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.widget.Toast

import android.media.MediaPlayer.OnCompletionListener

import android.widget.VideoView

import android.os.Build
import android.webkit.URLUtil

import android.widget.TextView





class PlayerActivity : AppCompatActivity() {

    private var sampleUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
//        "https://assets.dev.tripi.vn/assets/show/review/file/456483zxWOGMan/20220125_165009.mp4"
    lateinit var binding : ActivityPlayerBinding

    private val VIDEO_SAMPLE = "https://developers.google.com/training/images/tacoma_narrows.mp4"

    // Current playback position (in milliseconds).
    private var mCurrentPosition = 0

    // Tag for the instance state bundle.
    private val PLAYBACK_TIME = "play_time"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME)
        }

        // Set up the media controller widget and attach it to the video view.
        val controller = MediaController(this)
        controller.setMediaPlayer(binding.video)
        binding.video.setMediaController(controller)
    }

    override fun onPause() {
        super.onPause()

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            binding.video.pause()
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, binding.video.currentPosition)
    }

    private fun initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        // Buffer and decode the video sample.
        val videoUri = getMedia(/*sampleUrl*/"d.mp4")
        binding.video.setVideoURI(videoUri)

        // Listener for onPrepared() event (runs after the media is prepared).
        binding.video.setOnPreparedListener { // Hide buffering message.
            // Restore saved position, if available.
            if (mCurrentPosition > 0) {
                binding.video.seekTo(mCurrentPosition)
            } else {
                // Skipping to 1 shows the first frame of the video.
                binding.video.seekTo(1)
            }

            // Start playing!
            binding.video.start()
        }

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        binding.video.setOnCompletionListener {
            Toast.makeText(
                this@PlayerActivity,
                R.string.toast_message,
                Toast.LENGTH_SHORT
            ).show()

            // Return the video position to the start.
            binding.video.seekTo(0)
        }
    }


    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
    private fun releasePlayer() {
        binding.video.stopPlayback()
    }

    // Get a Uri for the media sample regardless of whether that sample is
    // embedded in the app resources or available on the internet.
    private fun getMedia(mediaName: String): Uri {
        return if (URLUtil.isValidUrl(mediaName)) {
            // Media name is an external URL.
            Uri.parse(mediaName)
        } else {

            // you can also put a video file in raw package and get file from there as shown below
            Uri.parse(
                "android.resource://" + packageName +
                        "/raw/" + mediaName
            )
        }
    }

}