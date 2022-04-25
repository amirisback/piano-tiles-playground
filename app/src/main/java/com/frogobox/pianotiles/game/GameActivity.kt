package com.frogobox.pianotiles.game

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import com.frogobox.pianotiles.MainActivity
import com.frogobox.pianotiles.databinding.ActivityGameBinding
import com.frogobox.sdk.view.FrogoActivity


class GameActivity : FrogoActivity<ActivityGameBinding>() {

    private lateinit var gameView: GameView
    private val TAG = GameActivity::class.java.simpleName

    override fun setupViewBinding(): ActivityGameBinding {
        return ActivityGameBinding.inflate(layoutInflater)
    }

    override fun setupOnCreate(savedInstanceState: Bundle?) {

        Log.d(TAG, "activity created")
        removeNotifBar()

        val speed = intent.getStringExtra("speed")
        val music = intent.getBooleanExtra("music", true)
        val vibration = intent.getBooleanExtra("vibration", true)

        GameView.music = music
        GameView.vibration = vibration

        if (speed != "") {
            GameTile.speed = speed!!.toInt()
        }

        gameView = GameView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        binding.rvGame.addView(gameView)

    }

    fun removeNotifBar() {
        // remove notification bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun setScoreText(score: String) {
        runOnUiThread {
            binding.tvScore.text = score
        }
    }

    fun goToGameOver() {
        runOnUiThread {
            Handler().postDelayed(object : Runnable {
                override fun run() {
                    val i = Intent(this@GameActivity, MainActivity::class.java)
                    startActivity(i) // Menghubungkan activity splashscren ke main activity dengan intent
                    finish() // Jeda selesai Splashscreen
                }

                private fun finish() {}
            }, 700)
        }
    }

    override fun onPause() {
        Log.d(TAG, "activity paused")
        super.onPause()
    }

    override fun onResume() {
        Log.d(TAG, "activity resumed")
        super.onResume()
    }

    override fun onDestroy() {
        Log.d(TAG, "activity destroyed")
        super.onDestroy()
    }

}
