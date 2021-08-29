package com.frogobox.pianotiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.frogobox.pianotiles.databinding.FragmentHighScoresBinding
import com.frogobox.pianotiles.databinding.ListItemBinding
import com.frogobox.sdk.core.FrogoFragment

/** Shows high scores */
class HighScoresFragment : FrogoFragment<FragmentHighScoresBinding>() {

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHighScoresBinding {
        return FragmentHighScoresBinding.inflate(inflater, container, false)
    }

    override fun setupViewModel() {}

    override fun setupUI(savedInstanceState: Bundle?) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.apply {
            val sharedPref = activity?.getSharedPreferences(
                getString(R.string.shared_preferences_name),
                AppCompatActivity.MODE_PRIVATE
            )
            val highScores = sharedPref?.all?.toSortedMap(compareBy<String> { it.toInt() })

            for (score in highScores!!.iterator()) {
                val item = ListItemBinding.inflate(inflater, highScoresTable, false)
                item.speed.text = score.key
                item.score.text = score.value.toString()
                highScoresTable.addView(item.root)
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

}