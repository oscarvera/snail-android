package com.oscarvera.snail.usecases.deskdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.model.domain.getEj
import com.oscarvera.snail.usecases.addcard.AddCardActivity
import com.oscarvera.snail.usecases.home.CardsAdapter
import com.oscarvera.snail.usecases.learning.LearningActivity
import com.oscarvera.snail.util.GridSpacingItemDecoration
import com.oscarvera.snail.util.Utils
import com.oscarvera.snail.util.extensions.getProperId
import kotlinx.android.synthetic.main.activity_desk_datail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


class DeskDetailActivity : AppCompatActivity() {

    lateinit var desksDetailViewModel: DeskDetailViewModel

    private var adapterCards: CardsAdapter? = null

    companion object {
        const val EXTRA_ID_DESK = "idDesk"
    }

    private var idDesk : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desk_datail)
        desksDetailViewModel = ViewModelProvider(this).get(DeskDetailViewModel::class.java)

        idDesk = intent.getStringExtra(EXTRA_ID_DESK)

        listCards.layoutManager = GridLayoutManager(this, 2)
        val spanCount = 2
        val spacing = (25 * resources.displayMetrics.density).roundToInt()
        listCards.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, false))

        desksDetailViewModel.cards.observe(this, Observer {

            val countStatesCards = Utils.countStatusDeskWithCards(it)
            text_tolearn.text = countStatesCards[StatusCard.TO_LEARN].toString()
            text_learning.text = countStatesCards[StatusCard.LEARNING].toString()
            text_learned.text = countStatesCards[StatusCard.LEARNED].toString()

            val titleSeparator = "${it.size} ${getString(R.string.name_card)}"
            title_separator_1.text = titleSeparator


            adapterCards = CardsAdapter(it, object : CardsAdapter.CardAdapterCallback {
                override fun onClick(card: Card) {

                }

                override fun onClickAddCard() {
                    newIntentAddCard()
                }

            })

            listCards.adapter = adapterCards

        })

        desksDetailViewModel.desk.observe(this, Observer {

            title_desk_detail.text = it.name


        })


        idDesk?.let {
            desksDetailViewModel.getDesk(idDesk = it)
            desksDetailViewModel.getCards(idDesk = it)

            home_fab_add_card.setOnClickListener {
                newIntentLearning()
            }

        }


        btn_back.setOnClickListener {
            finish()
        }


    }

    private fun newIntentAddCard() {
        idDesk?.let {
            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra(AddCardActivity.EXTRA_ID_DESK, idDesk)
            intent.putExtra(AddCardActivity.EXTRA_NAME_DESK, title_desk_detail.text)
            startActivity(intent)
        }

    }

    private fun newIntentLearning() {
        idDesk?.let {
            val intent = Intent(this, LearningActivity::class.java)
            intent.putExtra(LearningActivity.EXTRA_ID_DESK, idDesk)
            intent.putExtra(LearningActivity.EXTRA_NAME_DESK, title_desk_detail.text)
            startActivity(intent)
        }

    }

}