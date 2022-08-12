package com.oscarvera.snail.usecases.deskdetail

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.oscarvera.snail.R
import com.oscarvera.snail.databinding.ActivityDeskDetailBinding
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.usecases.home.CardsAdapter
import com.oscarvera.snail.util.*
import com.oscarvera.snail.util.customs.GridSpacingItemDecoration
import com.oscarvera.snail.util.customs.LoadingDialog
import com.oscarvera.snail.util.customs.Result
import com.oscarvera.snail.util.extensions.afterTextChanged


class DeskDetailActivity : AppCompatActivity() {

    lateinit var desksDetailViewModel: DeskDetailViewModel
    private lateinit var binding: ActivityDeskDetailBinding

    private var adapterCards: CardsAdapter? = null

    companion object {
        const val EXTRA_ID_DESK = "idDesk"
    }

    private var idDesk: String? = null
    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeskDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        desksDetailViewModel = ViewModelProvider(this)[DeskDetailViewModel::class.java]

        loadingDialog = LoadingDialog(this)

        idDesk = intent.getStringExtra(EXTRA_ID_DESK)
        sendEventWithDeskId(EventType.SHOWDESKVIEW, idDesk)

        binding.recyclerViewCards.layoutManager = GridLayoutManager(this, 2)

        binding.recyclerViewCards.addItemDecoration(
            GridSpacingItemDecoration(
                Configuration.spanCount,
                Configuration.getSpacing(resources),
                false
            )
        )

        desksDetailViewModel.cards.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    loadingDialog.finishLoadingDialog(true)
                    result.data?.let {
                        fillCardsData(it)
                    }
                }
                Result.Status.ERROR -> {


                }
                Result.Status.LOADING -> {
                    loadingDialog.showLoadingDialog()
                }
            }
        })

        desksDetailViewModel.desk.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    loadingDialog.finishLoadingDialog(true)
                    binding.layoutTopbar.titleTopBar.text = result.data?.name
                }
                Result.Status.LOADING -> loadingDialog.showLoadingDialog()
                else -> {}
            }

        })

        binding.layoutTopbar.btnOptions.visibility = View.VISIBLE
        binding.layoutTopbar.btnOptions.setOnClickListener {

            Dialogs.optionsBottomSheetDialog(this, layoutInflater, object : Dialogs.DeskSettingsDialog {
                override fun onDelete(dialogOptions: Dialog) {
                    dialogOptions.dismiss()
                    loadingDialog.setCallback(object : LoadingDialog.LoadingDialogCallback {
                        override fun onFinish(dialog: Dialog) {
                            dialog.dismiss()
                            finish()
                        }
                    })
                    loadingDialog.showLoadingDialog()
                    desksDetailViewModel.deleteDesk() {
                        loadingDialog.finishLoadingDialog()
                    }
                }

            })

        }


        idDesk?.let {
            desksDetailViewModel.getDesk(idDesk = it)
            desksDetailViewModel.getCards(idDesk = it)
        }


        binding.layoutTopbar.btnBack.setOnClickListener {
            finish()
        }


    }

    private fun fillCardsData(listCards: List<CardWithData>) {

        val countStatesCards = Utils.countStatusDeskWithCards(listCards)
        binding.textTolearn.text = countStatesCards[StatusCard.TO_LEARN].toString()
        binding.textLearning.text = countStatesCards[StatusCard.LEARNING].toString()
        binding.textLearned.text = countStatesCards[StatusCard.LEARNED].toString()

        val titleSeparator = "${listCards.size} ${getString(R.string.name_card)}"
        binding.titleSeparator1.text = titleSeparator


        adapterCards = CardsAdapter(listCards, object : CardsAdapter.CardAdapterCallback {
            override fun onClick(card: Card) {

            }

            override fun onClickAddCard() {
                newIntentAddCard()
            }

        })

        binding.recyclerViewCards.adapter = adapterCards


        if (countStatesCards[StatusCard.TO_LEARN]!! > 0) {
            //They have cards to learn
            binding.textNumberTolearn.text = countStatesCards[StatusCard.TO_LEARN].toString()
            binding.btnLearnDesk.visibility = View.VISIBLE
            binding.btnLearnDesk.setOnClickListener {
                newIntentLearning()
            }

        } else {
            binding.btnLearnDesk.visibility = View.GONE
        }

        binding.editTextSearch.afterTextChanged {

            val listFilter = listCards.filter { card ->
                val text1 = card.cardData?.get(0)?.text ?: ""
                val text2 = card.cardData?.get(1)?.text ?: ""
                text1.contains(it, true) || text2.contains(it, true)
            }
            adapterCards?.setChangeCards(listFilter)

        }
    }

    private fun newIntentAddCard() {
        idDesk?.let {
            Router.launchAddCardActivity(this, it, binding.layoutTopbar.titleTopBar.text.toString())
        }
    }

    private fun newIntentLearning() {
        idDesk?.let {
            Router.launchLearningActivity(this, it, binding.layoutTopbar.titleTopBar.text.toString())
        }
    }

}