package com.example.attendanceapp.presentation.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlowActivity
import com.example.attendanceapp.presentation.new_attendee.NewAttendeeDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel.checkAuthStatus()
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
                    mainViewModel.resultIsReady = true
                }

                is MainActivityViewModel.AuthState.LoggedOut -> {
                    navController.navigate(R.id.loginFragment)
                    mainViewModel.resultIsReady = true

                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val lsActiveFragments: List<Fragment> = supportFragmentManager.fragments
        for (fragmentActive in lsActiveFragments) {
            if (fragmentActive is NavHostFragment) {
                val lsActiveSubFragments: List<Fragment> =
                    fragmentActive.getChildFragmentManager().getFragments()
                for (fragmentActiveSub in lsActiveSubFragments) {
                    if (fragmentActiveSub is NewAttendeeDialog) {
                        fragmentActiveSub.onActivityResult(requestCode, resultCode, data)
                    }
                }
            }
        }

    }


}