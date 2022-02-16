package com.oscarvera.snail.usecases.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oscarvera.snail.R
import com.oscarvera.snail.usecases.home.desks.DesksFragment
import com.oscarvera.snail.usecases.home.settings.SettingsFragment
import com.oscarvera.snail.usecases.home.shared.SharedFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    private val desksFragment = DesksFragment()
    private val sharedFragment = SharedFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(desksFragment)
        animation_view.playAnimation()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        bottom_navigation_home.itemIconTintList = null
        bottom_navigation_home.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tab_home -> replaceFragment(desksFragment)
                R.id.tab_shared -> replaceFragment(sharedFragment)
                R.id.tab_settings -> replaceFragment(settingsFragment)
            }
            return@setOnItemSelectedListener true
        }

        /*home_fab_add_desk.setOnClickListener {

            showAddDeskDialog()

        }*/


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