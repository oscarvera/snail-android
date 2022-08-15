package com.oscarvera.snail.usecases.deskshareddetail

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.oscarvera.snail.R
import com.oscarvera.snail.databinding.ActivityDeskSharedDetailBinding
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.usecases.home.shared.SharedViewModel
import com.oscarvera.snail.util.Configuration
import com.oscarvera.snail.util.customs.GridSpacingItemDecoration
import com.oscarvera.snail.util.customs.LoadingDialog
import com.oscarvera.snail.util.customs.Result


class DeskSharedDetailActivity : AppCompatActivity() {

    lateinit var desksSharedDetailViewModel: DeskSharedDetailViewModel
    lateinit var sharedViewModel: SharedViewModel

    private lateinit var binding: ActivityDeskSharedDetailBinding

    private var adapterCards: CardsSharedAdapter? = null

    companion object {
        const val EXTRA_ID_DESK = "idRemoteDesk"
    }

    private var idRemoteDesk: String? = null

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeskSharedDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        desksSharedDetailViewModel =
            ViewModelProvider(this)[DeskSharedDetailViewModel::class.java]
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        idRemoteDesk = intent.getStringExtra(EXTRA_ID_DESK)

        binding.listCards.layoutManager = GridLayoutManager(this, 2)

        binding.listCards.addItemDecoration(
            GridSpacingItemDecoration(
                Configuration.spanCount,
                Configuration.getSpacing(resources),
                false
            )
        )

        desksSharedDetailViewModel.cards.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    loadingDialog.finishLoadingDialog(true)
                    result.data?.let {
                        fillDataCards(it)
                    }
                }
                Result.Status.ERROR -> {}
                Result.Status.LOADING -> loadingDialog.showLoadingDialog()
            }

        })

        desksSharedDetailViewModel.desk.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    loadingDialog.finishLoadingDialog(true)
                    result.data?.let {
                        fillDataDesk(it)
                    }
                }
                Result.Status.ERROR -> {}
                Result.Status.LOADING -> loadingDialog.showLoadingDialog()
            }

        })

        sharedViewModel.isDesksShared.observe(this, Observer {
            loadingDialog.finishLoadingDialog()
        })


        idRemoteDesk?.let {
            desksSharedDetailViewModel.getSharedDesk(it)
        }


        binding.layoutTopbar.btnBack.setOnClickListener {
            finish()
        }

    }

    private fun fillDataCards(list: List<Card>) {
        val titleSeparator = "${list.size} ${getString(R.string.name_card)}"
        binding.titleSeparator1.text = titleSeparator

        adapterCards = CardsSharedAdapter(list)

        binding.listCards.adapter = adapterCards
    }

    private fun fillDataDesk(desk: DeskShared) {
        binding.layoutTopbar.titleTopBar.text = desk.name

        binding.textDownload.text = desk.timesDownloaded.toString()
        binding.textOwner.text = desk.userName
        binding.textUpload.text = desk.uploaded

        binding.btnDownloadDesk.setOnClickListener {
            sharedViewModel.downloadDesk(desk)
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