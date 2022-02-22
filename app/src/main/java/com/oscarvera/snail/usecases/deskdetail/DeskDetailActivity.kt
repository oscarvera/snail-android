package com.oscarvera.snail.usecases.deskdetail

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.usecases.home.CardsAdapter
import com.oscarvera.snail.util.*
import com.oscarvera.snail.util.extensions.afterTextChanged
import kotlinx.android.synthetic.main.activity_desk_detail.*
import kotlinx.android.synthetic.main.fragment_shared.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import kotlin.math.roundToInt


class DeskDetailActivity : AppCompatActivity() {

    lateinit var desksDetailViewModel: DeskDetailViewModel

    private var adapterCards: CardsAdapter? = null

    companion object {
        const val EXTRA_ID_DESK = "idDesk"
    }

    private var idDesk: String? = null
    lateinit var loadingDialog : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desk_detail)
        desksDetailViewModel = ViewModelProvider(this).get(DeskDetailViewModel::class.java)

        loadingDialog = LoadingDialog(this)

        idDesk = intent.getStringExtra(EXTRA_ID_DESK)

        recyclerViewCards.layoutManager = GridLayoutManager(this, 2)
        val spanCount = 2
        val spacing = (25 * resources.displayMetrics.density).roundToInt()
        recyclerViewCards.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, false))

        desksDetailViewModel.cards.observe(this, Observer { listCards ->

            val countStatesCards = Utils.countStatusDeskWithCards(listCards)
            text_tolearn.text = countStatesCards[StatusCard.TO_LEARN].toString()
            text_learning.text = countStatesCards[StatusCard.LEARNING].toString()
            text_learned.text = countStatesCards[StatusCard.LEARNED].toString()

            val titleSeparator = "${listCards.size} ${getString(R.string.name_card)}"
            title_separator_1.text = titleSeparator


            adapterCards = CardsAdapter(listCards, object : CardsAdapter.CardAdapterCallback {
                override fun onClick(card: Card) {

                }

                override fun onClickAddCard() {
                    newIntentAddCard()
                }

            })

            recyclerViewCards.adapter = adapterCards


            if (countStatesCards[StatusCard.TO_LEARN]!! > 0) {
                //They have cards to learn
                text_number_tolearn.text = countStatesCards[StatusCard.TO_LEARN].toString()
                btn_learn_desk.visibility = View.VISIBLE
                btn_learn_desk.setOnClickListener {
                    newIntentLearning()
                }

            } else {
                btn_learn_desk.visibility = View.GONE
            }

            edit_text_search.afterTextChanged {

                val listFilter = listCards.filter { card ->
                    val text1 = card.cardData?.get(0)?.text ?: ""
                    val text2 = card.cardData?.get(1)?.text ?: ""
                    text1.contains(it, true) || text2.contains(it,true)
                }
                adapterCards?.setChangeCards(listFilter)

            }

        })

        desksDetailViewModel.desk.observe(this, Observer {

            title_top_bar.text = it.name


        })

        btn_options.visibility = View.VISIBLE
        btn_options.setOnClickListener {

            Dialogs.optionsBottomSheetDialog(this,layoutInflater,object : Dialogs.DeskSettingsDialog{
                override fun onDelete(dialogOptions: Dialog) {
                    dialogOptions.dismiss()
                    loadingDialog.setCallback(object : LoadingDialog.LoadingDialogCallback {
                        override fun onFinish(dialog: Dialog) {
                            dialog.dismiss()
                            finish()
                        }
                    })
                    loadingDialog.showLoadingDialog()
                    desksDetailViewModel.deleteDesk(idDesk!!) {
                        loadingDialog.finishLoadingDialog()
                    }
                }

            })

        }


        idDesk?.let {
            desksDetailViewModel.getDesk(idDesk = it)
            desksDetailViewModel.getCards(idDesk = it)
        }


        btn_back.setOnClickListener {
            finish()
        }


    }

    private fun newIntentAddCard() {
        idDesk?.let {
            Router.launchAddCardActivity(this, it, title_top_bar.text.toString())
        }
    }

    private fun newIntentLearning() {
        idDesk?.let {
            Router.launchLearningActivity(this, it, title_top_bar.text.toString())
        }
    }

    override fun onRestart() {
        super.onRestart()
        idDesk?.let {
            desksDetailViewModel.getDesk(idDesk = it)
            desksDetailViewModel.getCards(idDesk = it)
        }
    }


}