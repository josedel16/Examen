package com.gmail.jmdm1601.examen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.gmail.jmdm1601.examen.core.Constants
import com.gmail.jmdm1601.examen.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.movies))
        mBinding.topAppBar.setupWithNavController(navController, appBarConfiguration)
        mBinding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.movies -> mBinding.bottomNavigation.visibility = View.VISIBLE
                R.id.movieDetailFragment -> {
                    mBinding.bottomNavigation.visibility = View.GONE
                    mBinding.topAppBar.title = arguments?.getString(Constants.TITLE, "")
                }
                else -> mBinding.bottomNavigation.visibility = View.GONE
            }
        }
    }
}