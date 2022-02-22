package com.oscarvera.snail.usecases.crossdata

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.util.LoadingDialog
import com.oscarvera.snail.util.Router
import kotlinx.android.synthetic.main.activity_cross_data.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import kotlinx.android.synthetic.main.layout_top_bar.btn_back

class CrossDataActivity : AppCompatActivity() {

    lateinit var crossDataViewModel: CrossDataViewModel

    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_data)

        crossDataViewModel = ViewModelProvider(this).get(CrossDataViewModel::class.java)

        loadingDialog = LoadingDialog(this)


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
                                Log.d("ERROR",it.exception.toString())
                                //There is an error with google account
                                //TODO: Show error message
                            }
                        }
                }
            } catch (e: ApiException) {
                //There is an error with Intent or google sign in
                //TODO: Show error message
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


        btn_back.setOnClickListener {
            finish()
        }

        btn_migrate_data.setOnClickListener {
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