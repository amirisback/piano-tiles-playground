package com.frogobox.pianotiles

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.frogobox.pianotiles.databinding.FragmentMainBinding
import com.frogobox.pianotiles.game.GameActivity
import com.frogobox.sdk.core.FrogoFragment

/** Homepage of the app */
class MainFragment : FrogoFragment<FragmentMainBinding>() {

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun setupViewModel() {}

    override fun setupUI(savedInstanceState: Bundle?) {

        binding.apply {
            button.setOnClickListener {
                val speed = editText.text.toString()
                val music = musicBox.isChecked
                val vibration = vibrationBox.isChecked

                if (speed == "" || speed == "0") {
                    showToast("You have to select a speed")
                } else {
                    val intent = Intent(context, GameActivity::class.java).apply {
                        putExtra("speed", speed)
                        putExtra("music", music)
                        putExtra("vibration", vibration)
                    }
                    startActivity(intent)
                }
            }
        }
    }

}