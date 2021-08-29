package com.frogobox.pianotiles

import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.frogobox.pianotiles.databinding.ActivityMainBinding
import com.frogobox.sdk.core.FrogoActivity


class MainActivity : FrogoActivity<ActivityMainBinding>() {

    private lateinit var navController: NavController

    override fun setupViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setupViewModel() {}

    override fun setupUI(savedInstanceState: Bundle?) {
        binding.apply {

            // set toolbar
            navController = findNavController(R.id.pianoTilesNavHostFragment)
            NavigationUI.setupActionBarWithNavController(
                this@MainActivity,
                navController,
                drawerLayout
            )
            NavigationUI.setupWithNavController(navView, navController)

            // menu can only be opened in main menu
            navController.addOnDestinationChangedListener { controller, destination, _ ->
                if (destination.id == controller.graph.startDestination) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

}
