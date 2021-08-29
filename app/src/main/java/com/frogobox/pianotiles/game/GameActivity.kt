package com.frogobox.pianotiles.game

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import com.frogobox.pianotiles.R
import com.frogobox.pianotiles.databinding.ActivityGameBinding
import com.frogobox.sdk.core.FrogoActivity
import com.frogobox.pianotiles.MainActivity

import android.content.Intent
import android.os.Handler


class GameActivity : FrogoActivity<ActivityGameBinding>() {

    private lateinit var gameView: GameView
    private lateinit var img: View

    override fun setupViewBinding(): ActivityGameBinding {
        return ActivityGameBinding.inflate(layoutInflater)
    }

    override fun setupViewModel() {}

    override fun setupUI(savedInstanceState: Bundle?) {

        Log.d("ati", "activity created")
        removeNotifBar()

        val speed = intent.getStringExtra("speed")
        val music = intent.getBooleanExtra("music", true)
        val vibration = intent.getBooleanExtra("vibration", true)

        GameView.music = music
        GameView.vibration = vibration

        if (speed != "") {
            GameTile.speed = speed!!.toInt()
        }

        val screen = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        gameView = GameView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        screen.addView(gameView)

        img = layoutInflater.inflate(R.layout.centered_image, screen, false)
        img.visibility = View.GONE

        img.setOnClickListener {
            gameView.restart()
        }

        screen.addView(img)

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
        Log.d("ati", "activity paused")
        super.onPause()
    }

    override fun onResume() {
        Log.d("ati", "activity resumed")
        super.onResume()
    }

    override fun onDestroy() {
        Log.d("ati", "activity destroyed")
        gameView.destroy()
        super.onDestroy()
    }

}
