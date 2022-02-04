package com.oscarvera.snail.usecases.home

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.oscarvera.snail.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    private val desksFragment = DesksFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        bottom_navigation_home.itemIconTintList = null
        bottom_navigation_home.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tab_home -> replaceFragment(desksFragment)
                R.id.tab_charts -> replaceFragment(desksFragment)
                R.id.tab_settings -> replaceFragment(desksFragment)
                R.id.tab_online -> replaceFragment(desksFragment)
            }
            return@setOnItemSelectedListener true
        }

        home_fab_add_desk.setOnClickListener {

            showAddDeskDialog()

        }


    }

    private fun showAddDeskDialog() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_desk)
        dialog.show()
        val btnAdd = dialog.findViewById<Button>(R.id.btn_addDesk)
        val textName = dialog.findViewById<EditText>(R.id.edittext_name)
        btnAdd.setOnClickListener {
            dialog.dismiss()
            //TODO: Create desk and validate String name
            mainViewModel.addNewDesk(textName.text.toString()) {
                Snackbar.make(
                    frame_container,
                    R.string.dialog_newdesk_action_added_success,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
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