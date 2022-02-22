package com.oscarvera.snail.usecases.home.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.oscarvera.snail.R
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.provider.preferences.PrefManager
import com.oscarvera.snail.util.Dialogs
import com.oscarvera.snail.util.Router
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {

    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        fillFields(view)

        view.btn_quantifier.setOnClickListener {
            activity?.let {
                Dialogs.createChangeNumber(
                    it,
                    getString(R.string.setting_main_dialog_quantifier_number_title),
                    getString(R.string.setting_main_dialog_quantifier_number_subtitle),
                    PrefManager.quantifierNumber.toString(),
                    getString(R.string.setting_main_dialog_btn_change)
                ) { newNumber ->
                    if (newNumber > 0){
                        PrefManager.quantifierNumber = newNumber
                        fillFields(view)
                    }
                }
            }

        }

        view.btn_max_quantifier.setOnClickListener {
            activity?.let {
                Dialogs.createChangeNumber(
                    it,
                    getString(R.string.setting_main_dialog_quantifier_max_title),
                    getString(R.string.setting_main_dialog_quantifier_max_subtitle),
                    PrefManager.maxQuantifierToBeLearned.toString(),
                    getString(R.string.setting_main_dialog_btn_change)
                ) { newNumber ->
                    if (newNumber > 0){
                        PrefManager.maxQuantifierToBeLearned = newNumber
                        fillFields(view)
                    }
                }
            }

        }

        view.btn_online_name.setOnClickListener {
            activity?.let {
                Dialogs.createChangeText(
                    it,
                    getString(R.string.setting_main_dialog_online_name_title),
                    getString(R.string.setting_main_dialog_online_name_subtitle),
                    PrefManager.userNameShare ?: getString(R.string.void_username_desk_shared),
                    getString(R.string.setting_main_dialog_btn_change)
                ) { newName ->
                    if (newName.isNotEmpty()){
                        PrefManager.userNameShare = newName
                        fillFields(view)
                    }
                }
            }
        }

        if (SessionManager.isLocalMode()) {
            view.btn_turn_online.setOnClickListener {
                activity?.let {
                    Router.launchDataCrossActivity(it)
                }
            }
        } else {
            view.btn_turn_online.visibility = View.GONE
        }


        if (SessionManager.isLocalMode()) {
            view.btn_delete_data.setOnClickListener {
                settingsViewModel.deleteAllDesks()
            }
        }else{
            view.btn_delete_data.visibility = View.GONE
        }



        view.btn_close_session.setOnClickListener {
            if (SessionManager.isLocalMode()) {
                Router.launchLoginIntent(activity)
                SessionManager.signout()
            } else {
                FirebaseAuth.getInstance().signOut()
                SessionManager.signout()
                Router.launchLoginIntent(activity)
            }

        }


        return view
    }

    fun fillFields(view : View){
        view.text_quantifier.text = PrefManager.quantifierNumber.toString()
        view.text_max_quantifier.text = PrefManager.maxQuantifierToBeLearned.toString()
        view.text_online_name.text =
            PrefManager.userNameShare ?: getString(R.string.void_username_desk_shared)
    }


    override fun onResume() {
        super.onResume()
        view?.let {
            fillFields(it)
        }

    }

}