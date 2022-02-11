package com.example.attendanceapp.presentation.splash_screen

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.TransitionManager
import com.example.attendanceapp.databinding.FragmentSplashBinding
import com.example.attendanceapp.presentation.main_activity.MainActivityViewModel
import com.google.android.material.transition.MaterialFade


class SplashFragment : Fragment() {

    val mainViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var splashBinding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        splashBinding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = splashBinding.root
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        Handler().postDelayed({
            val materialFade = MaterialFade().apply {
                duration = 1000L
            }
            TransitionManager.beginDelayedTransition(container!!, materialFade)
            splashBinding.textView2.visibility = View.VISIBLE
        }, 1000)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}