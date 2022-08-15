package com.oscarvera.snail.usecases.sharedesk

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.databinding.ActivityShareDeskBinding
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.provider.preferences.PrefManager
import com.oscarvera.snail.util.Dialogs
import com.oscarvera.snail.util.customs.LoadingDialog

class ShareDeskActivity : AppCompatActivity() {

    lateinit var shareViewModel: DeskShareViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null

    private lateinit var binding: ActivityShareDeskBinding

    private var adapterShare: DesksShareAdapter? = null
    private var listDesks: List<DeskWithCards>? = null

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShareDeskBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        shareViewModel = ViewModelProvider(this)[DeskShareViewModel::class.java]

        binding.layoutTopbar.titleTopBar.text = getString(R.string.share_desk_title)
        layoutManager = LinearLayoutManager(this)
        binding.listDeskToShare.layoutManager = layoutManager

        binding.layoutTopbar.btnBack.setOnClickListener {
            finish()
        }

        shareViewModel.desks.observe(this, Observer {

            listDesks = it
            adapterShare =
                DesksShareAdapter(
                    it,
                    object : DesksShareAdapter.DeskShareAdapterCallback {
                        override fun onClick(desk: DeskWithCards, position: Int) {
                            adapterShare?.setPositionChecked(position)
                            adapterShare?.notifyDataSetChanged()
                            binding.btnShareDesk.visibility = View.VISIBLE
                        }

                    })
            binding.listDeskToShare.adapter = adapterShare


        })

        shareViewModel.desksShared.observe(this, Observer {

            loadingDialog.finishLoadingDialog()

        })


        shareViewModel.getDeskWithCards()

        binding.btnShareDesk.setOnClickListener {
            listDesks?.let { desks ->
                adapterShare?.getPositionChecked()?.let { positionDesk ->
                    shareDeskNameUser(desks[positionDesk])
                }
            }
        }

    }

    private fun shareDeskNameUser(desk: DeskWithCards) {
        if (PrefManager.userNameShare.isNullOrEmpty()) {
            Dialogs.createShareNameDialog(this, object : Dialogs.NewShareNameDialog {
                override fun shareDesk(name: String, dialog: Dialog) {
                    if (name.isEmpty()) {
                        shareViewModel.shareDesk(
                            desk = desk,
                            getString(R.string.void_username_desk_shared)
                        )
                    } else {
                        PrefManager.userNameShare = name
                        shareViewModel.shareDesk(desk = desk, name)
                    }
                    loadingDialog.setCallback(object : LoadingDialog.LoadingDialogCallback {
                        override fun onFinish(dialog: Dialog) {
                            dialog.dismiss()
                            finish()
                        }
                    })
                    loadingDialog.showLoadingDialog()
                    dialog.dismiss()
                }

            })
        } else {
            shareViewModel.shareDesk(desk = desk, PrefManager.userNameShare!!)
            loadingDialog.setCallback(object : LoadingDialog.LoadingDialogCallback {
                override fun onFinish(dialog: Dialog) {
                    dialog.dismiss()
                    finish()
                }
            })
            loadingDialog.showLoadingDialog()
        }

    }


}