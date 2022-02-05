package com.oscarvera.snail.usecases.deskdetail

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.getEj
import com.oscarvera.snail.usecases.home.CardsAdapter
import com.oscarvera.snail.util.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_desk_datail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


class DeskDetailActivity : AppCompatActivity() {

    lateinit var desksDetailViewModel: DeskDetailViewModel

    private var adapterCards: CardsAdapter? = null

    companion object {
        const val EXTRA_ID_DESK = "idDesk"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desk_datail)
        desksDetailViewModel = ViewModelProvider(this).get(DeskDetailViewModel::class.java)


        listCards.layoutManager = GridLayoutManager(this, 2)

        val spanCount = 2
        val spacing = (25 * resources.displayMetrics.density).roundToInt()
        listCards.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, false))

        desksDetailViewModel.cards.observe(this, Observer {

            adapterCards = CardsAdapter(it, object : CardsAdapter.CardAdapterCallback {
                override fun onClick(card: Card) {

                }

            })

            listCards.adapter = adapterCards

        })

        desksDetailViewModel.desk.observe(this, Observer {

            title_desk_detail.text = it.name

        })

        val idDesk = intent.getStringExtra(EXTRA_ID_DESK)
        idDesk?.let {
            desksDetailViewModel.getDesk(idDesk = it)
            desksDetailViewModel.getCards(idDesk = it)

            home_fab_add_card.setOnClickListener {

                desksDetailViewModel.addCard(idDesk, getEj()) {
                    Snackbar.make(
                        activity_container,
                        R.string.dialog_newcard_action_added_success,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }

        }


        btn_back.setOnClickListener {
            finish()
        }




    }

}