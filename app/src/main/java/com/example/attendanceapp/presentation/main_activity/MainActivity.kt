package com.example.attendanceapp.presentation.main_activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.attendanceapp.R
import com.example.attendanceapp.core.utils.collectLatestLifecycleFlowActivity
import com.example.attendanceapp.presentation.new_attendee.NewAttendeeDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val mainViewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setupNavigation()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment?
        navController = navHostFragment!!.navController
        navController.navigate(R.id.splashFragment)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.checkAuthStatus()
        // waitForViewModel()

        Handler().postDelayed({
            handleNavigation()
        }, 3500)
        //android:theme="@android:style/Theme.Light"
        //android:theme="@style/Theme.App.Starting"
    }


    fun handleNavigation() {

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

    fun setActionBarTitle(title: String?) {
        Objects.requireNonNull(supportActionBar)?.title = title
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