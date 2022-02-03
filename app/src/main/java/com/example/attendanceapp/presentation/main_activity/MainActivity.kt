package com.example.attendanceapp.presentation.main_activity

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlowActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waitForViewModel()
        handleNavigation()


    }

    fun waitForViewModel() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (mainViewModel.resultIsReady) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            }
        )
    }

    fun handleNavigation() {
        val navController = findNavController(R.id.myNavHostFragment)
        collectLatestLifecycleFlowActivity(mainViewModel.authFlowState) { result ->
            when (result) {
                is MainActivityViewModel.AuthState.LoggedIn -> {
                    navController.navigate(R.id.eventListFragment)
                }

                is MainActivityViewModel.AuthState.LoggedOut -> {
                    navController.navigate(R.id.loginFragment)

                }

            }
        }
    }
}