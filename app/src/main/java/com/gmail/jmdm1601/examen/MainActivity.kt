package com.gmail.jmdm1601.examen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.gmail.jmdm1601.examen.core.Constants
import com.gmail.jmdm1601.examen.databinding.ActivityMainBinding
import com.gmail.jmdm1601.examen.services.LocationService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var isGranted = true
            it.forEach { (s, b) -> isGranted = isGranted && b }

            if (isGranted) {
                if (!LocationService.isServiceStarted) {
                    ContextCompat.startForegroundService(
                        this,
                        Intent(this, LocationService::class.java)
                    )
                }
            } else {
                showMaterialDialogNoPermissions()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.movies, R.id.images, R.id.location))
        mBinding.topAppBar.setupWithNavController(navController, appBarConfiguration)
        mBinding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.movies -> mBinding.bottomNavigation.visibility = View.VISIBLE
                R.id.images -> mBinding.bottomNavigation.visibility = View.VISIBLE
                R.id.location -> mBinding.bottomNavigation.visibility = View.VISIBLE
                R.id.movieDetailFragment -> {
                    mBinding.bottomNavigation.visibility = View.GONE
                    mBinding.topAppBar.title = arguments?.getString(Constants.TITLE, "")
                }
                else -> mBinding.bottomNavigation.visibility = View.GONE
            }
        }

        if (!checkPermission()) {
            requestPermission()
        } else {
            ContextCompat.startForegroundService(
                this,
                Intent(this, LocationService::class.java)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, LocationService::class.java))
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val result1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    private fun showMaterialDialogNoPermissions() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.dialog_permissions_title))
            .setMessage(getString(R.string.dialog_permissions_content_location))
            .setNegativeButton(getString(R.string.settings)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts(Constants.PACKAGE, packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setPositiveButton(getString(R.string.ok), null)
            .show()
    }
}