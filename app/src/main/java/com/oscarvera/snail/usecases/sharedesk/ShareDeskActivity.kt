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
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.provider.preferences.PrefManager
import com.oscarvera.snail.util.Dialogs
import com.oscarvera.snail.util.LoadingDialog
import kotlinx.android.synthetic.main.activity_share_desk.*
import kotlinx.android.synthetic.main.layout_top_bar.*

class ShareDeskActivity : AppCompatActivity() {

    lateinit var shareViewModel: DeskShareViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null

    private var adapterShare: DesksShareAdapter? = null
    private var listDesks: List<DeskWithCards>? = null

    lateinit var loadingDialog : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_desk)

        shareViewModel = ViewModelProvider(this).get(DeskShareViewModel::class.java)

        loadingDialog = LoadingDialog(this)

        title_top_bar.text = getString(R.string.share_desk_title)
        layoutManager = LinearLayoutManager(this)
        listDeskToShare.layoutManager = layoutManager

        btn_back.setOnClickListener {
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
                            btn_share_desk.visibility = View.VISIBLE
                        }

                    })
            listDeskToShare.adapter = adapterShare


        })

        shareViewModel.desksShared.observe(this, Observer {

            loadingDialog.finishLoadingDialog()

        })


        shareViewModel.getDeskWithCards()

        btn_share_desk.setOnClickListener {
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