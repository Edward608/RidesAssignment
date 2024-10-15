package com.edwardwongtl.rides

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.edwardwongtl.rides.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Delegate back button handling to Navigation Controller
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!findNavController(binding.navHostFragment.id).navigateUp()) {
                    finish()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val navController = findNavController(binding.navHostFragment.id)
        val appConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appConfiguration)
    }
}