package com.frogobox.pianotiles.game

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import com.frogobox.pianotiles.R
import com.frogobox.pianotiles.databinding.ActivityGameBinding
import com.frogobox.sdk.core.FrogoActivity

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
            Tile.speed = speed!!.toInt()
        }

        val screen = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        gameView = GameView(this)
        gameView.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
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


    fun showReplayButton() {
        this@GameActivity.runOnUiThread {
            img.visibility = View.VISIBLE
        }
    }

    fun hideReplayButton() {
        this@GameActivity.runOnUiThread {
            img.visibility = View.GONE
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
