package com.frogobox.pianotiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.frogobox.pianotiles.databinding.FragmentHighScoresBinding
import com.frogobox.pianotiles.databinding.ListItemBinding
import com.frogobox.sdk.view.FrogoFragment

/** Shows high scores */
class HighScoresFragment : FrogoFragment<FragmentHighScoresBinding>() {

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHighScoresBinding {
        return FragmentHighScoresBinding.inflate(inflater, container, false)
    }

    override fun setupOnViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            val sharedPref = activity?.getSharedPreferences(
                getString(R.string.shared_preferences_name),
                AppCompatActivity.MODE_PRIVATE
            )
            val highScores = sharedPref?.all?.toSortedMap(compareBy<String> { it.toInt() })

            for (score in highScores!!.iterator()) {
                val item = ListItemBinding.inflate(LayoutInflater.from(highScoresTable.context), highScoresTable, false)
                item.speed.text = score.key
                item.score.text = score.value.toString()
                highScoresTable.addView(item.root)
            }
        }

    }

}