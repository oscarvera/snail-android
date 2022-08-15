package com.oscarvera.snail.usecases.crossdata

import android.app.Dialog
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.oscarvera.snail.R
import com.oscarvera.snail.databinding.ActivityCrossDataBinding
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.util.*
import com.oscarvera.snail.util.customs.LoadingDialog

class CrossDataActivity : AppCompatActivity() {

    lateinit var crossDataViewModel: CrossDataViewModel

    private lateinit var binding: ActivityCrossDataBinding

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCrossDataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        crossDataViewModel = ViewModelProvider(this)[CrossDataViewModel::class.java]

        sendEvent(EventType.MIGRATESCREEN, null)

        val resultSignInGoogle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.let {
                    val credencial = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credencial)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                account.id?.let { id ->
                                    SessionManager.setIdFirebase(id)
                                    SessionManager.setLocalMode(false)
                                    loadingDialog.setCallback(object : LoadingDialog.LoadingDialogCallback {
                                        override fun onFinish(dialog: Dialog) {
                                            dialog.dismiss()
                                            Router.launchMainActivity(this@CrossDataActivity)
                                        }
                                    })
                                    loadingDialog.showLoadingDialog()
                                    crossDataViewModel.setLocaltoRemote()
                                }

                            } else {
                                //There is an error with google account
                                sendErrorEvent(CrossDataActivity::class.java.name,it.exception.toString())
                                Dialogs.createErrorDialog(this)
                            }
                        }
                }
            } catch (e: ApiException) {
                //There is an error with Intent or google sign in
                sendErrorEvent(CrossDataActivity::class.java.name,e.message)
                Dialogs.createErrorDialog(this)
            }
        }


        crossDataViewModel.doSignIn.observe(this, Observer {

            loadingDialog.finishLoadingDialog()

            val googleConf = GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestId()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            val signInIntent = googleClient.signInIntent
            resultSignInGoogle.launch(signInIntent)

        })

        crossDataViewModel.migrationFinished.observe(this, Observer {
            loadingDialog.finishLoadingDialog()
        })


        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnMigrateData.setOnClickListener {
            loadingDialog.setCallback(object : LoadingDialog.LoadingDialogCallback {
                override fun onFinish(dialog: Dialog) {
                    dialog.dismiss()
                    //finish()
                }
            })
            loadingDialog.showLoadingDialog()
            crossDataViewModel.getLocalDesks()

        }

    }


}