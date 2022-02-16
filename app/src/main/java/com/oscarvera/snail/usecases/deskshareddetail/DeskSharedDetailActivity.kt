package com.oscarvera.snail.usecases.deskshareddetail

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.oscarvera.snail.R
import com.oscarvera.snail.usecases.home.shared.SharedViewModel
import com.oscarvera.snail.util.Dialogs
import com.oscarvera.snail.util.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_desk_shared_detail.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import kotlin.math.roundToInt


class DeskSharedDetailActivity : AppCompatActivity() {

    lateinit var desksSharedDetailViewModel: DeskSharedDetailViewModel
    lateinit var sharedViewModel: SharedViewModel

    private var adapterCards: CardsSharedAdapter? = null

    companion object {
        const val EXTRA_ID_DESK = "idRemoteDesk"
    }

    private var idRemoteDesk: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desk_shared_detail)
        desksSharedDetailViewModel =
            ViewModelProvider(this).get(DeskSharedDetailViewModel::class.java)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        idRemoteDesk = intent.getStringExtra(EXTRA_ID_DESK)

        listCards.layoutManager = GridLayoutManager(this, 2)
        val spanCount = 2
        val spacing = (25 * resources.displayMetrics.density).roundToInt()
        listCards.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, false))

        desksSharedDetailViewModel.cards.observe(this, Observer {


            val titleSeparator = "${it.size} ${getString(R.string.name_card)}"
            title_separator_1.text = titleSeparator

            adapterCards = CardsSharedAdapter(it)

            listCards.adapter = adapterCards

        })

        desksSharedDetailViewModel.desk.observe(this, Observer { desk ->

            title_top_bar.text = desk.name

            text_download.text = desk.timesDownloaded.toString()
            text_owner.text = desk.userName
            text_upload.text = desk.uploaded

            btn_download_desk.setOnClickListener {
                sharedViewModel.downloadDesk(desk)
                //TODO: Start animation loading
                Dialogs.createLoadingDialog(this, object : Dialogs.LoadingDialog {
                    override fun onFinish(dialog: Dialog) {
                        dialog.dismiss()
                        finish()
                    }

                })
            }

        })

        sharedViewModel.isDesksShared.observe(this, Observer {
            //TODO: Stop animation
        })




        idRemoteDesk?.let {
            desksSharedDetailViewModel.getSharedDesk(it)
        }


        btn_back.setOnClickListener {
            finish()
        }


    }

}