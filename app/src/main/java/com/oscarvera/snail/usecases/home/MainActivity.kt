package com.oscarvera.snail.usecases.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oscarvera.snail.R
import com.oscarvera.snail.databinding.ActivityMainBinding
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.usecases.home.desks.DesksFragment
import com.oscarvera.snail.usecases.home.settings.SettingsFragment
import com.oscarvera.snail.usecases.home.shared.SharedFragment
import com.oscarvera.snail.util.Router

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private val desksFragment = DesksFragment()
    private val sharedFragment = SharedFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        replaceFragment(desksFragment)
        binding.animationView.playAnimation()
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.bottomNavigationHome.itemIconTintList = null
        binding.bottomNavigationHome.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tab_home -> replaceFragment(desksFragment)
                R.id.tab_shared -> {
                    if (SessionManager.isLocalMode()){
                        Router.launchDataCrossActivity(this)
                    }else{
                        replaceFragment(sharedFragment)
                    }
                }
                R.id.tab_settings -> replaceFragment(settingsFragment)
            }
            return@setOnItemSelectedListener true
        }


    }



    private fun replaceFragment(fragment: Fragment) {
        fragment.let {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, it)
                commit()
            }
        }

    }
}