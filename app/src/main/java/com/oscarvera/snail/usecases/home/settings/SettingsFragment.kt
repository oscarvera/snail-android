package com.oscarvera.snail.usecases.home.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.oscarvera.snail.R
import com.oscarvera.snail.databinding.FragmentSettingsBinding
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.provider.preferences.PrefManager
import com.oscarvera.snail.util.Dialogs
import com.oscarvera.snail.util.Router


class SettingsFragment : Fragment() {

    lateinit var settingsViewModel: SettingsViewModel

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_settings, container, false)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        fillFields()

        binding.btnQuantifier.setOnClickListener {
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
                        fillFields()
                    }
                }
            }

        }

        binding.btnMaxQuantifier.setOnClickListener {
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
                        fillFields()
                    }
                }
            }

        }

        binding.btnOnlineName.setOnClickListener {
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
                        fillFields()
                    }
                }
            }
        }

        if (SessionManager.isLocalMode()) {
            binding.btnTurnOnline.setOnClickListener {
                activity?.let {
                    Router.launchDataCrossActivity(it)
                }
            }
        } else {
            binding.btnTurnOnline.visibility = View.GONE
        }


        if (SessionManager.isLocalMode()) {
            binding.btnDeleteData.setOnClickListener {
                settingsViewModel.deleteAllDesks()
            }
        }else{
            binding.btnDeleteData.visibility = View.GONE
        }



        binding.btnCloseSession.setOnClickListener {
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

    fun fillFields(){
        binding.textQuantifier.text = PrefManager.quantifierNumber.toString()
        binding.textMaxQuantifier.text = PrefManager.maxQuantifierToBeLearned.toString()
        binding.textOnlineName.text =
            PrefManager.userNameShare ?: getString(R.string.void_username_desk_shared)
    }


    override fun onResume() {
        super.onResume()
        view?.let {
            fillFields()
        }

    }

}