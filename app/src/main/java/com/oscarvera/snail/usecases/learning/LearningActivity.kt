package com.oscarvera.snail.usecases.learning

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.oscarvera.snail.R
import kotlinx.android.synthetic.main.activity_learning.*
import kotlinx.android.synthetic.main.layout_top_bar.*

class LearningActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID_DESK = "idDesk"
        const val EXTRA_NAME_DESK = "nameDesk"
    }

    lateinit var learningVM: LearningViewModel

    private var idDesk: String? = null
    private var nameDesk: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        learningVM = ViewModelProvider(this).get(LearningViewModel::class.java)

        idDesk = intent.getStringExtra(EXTRA_ID_DESK)
        nameDesk = intent.getStringExtra(EXTRA_NAME_DESK)
        title_top_bar.text = nameDesk

        learningVM.card.observe(this, Observer {

            resetAnswerView()

            val card_data = it.cardData
            val data = card_data!!.first().text
            val data2 = card_data[1].text

            card_text1.text = data
            card_text2.text = data2

        })

        learningVM.noMoreCards.observe(this, Observer {
            noMoreCards()

        })



        idDesk?.let { idDesk ->
            learningVM.getCards(idDesk)
        }

        btn_show.setOnClickListener {
            showAnswerView()
        }

        btn_learned.setOnClickListener {
            if (btn_not_learned.visibility == View.VISIBLE) {
                //Card learned
                learningVM.setResultCard(true)
            } else {
                //Show answer
                showAnswerView()
            }

        }

        btn_not_learned.setOnClickListener {
            learningVM.setResultCard(false)
        }

        btn_back.setOnClickListener {
            finish()
        }


    }

    private fun resetAnswerView() {
        btn_not_learned.visibility = View.INVISIBLE
        im_check.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_arrow_bottom))
        card_text2.visibility = View.GONE
        btn_show.visibility = View.VISIBLE
    }

    private fun showAnswerView() {
        btn_not_learned.visibility = View.VISIBLE
        im_check.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_check))
        card_text2.visibility = View.VISIBLE
        btn_show.visibility = View.GONE
    }

    private fun noMoreCards() {
        btn_not_learned.visibility = View.INVISIBLE
        btn_learned.visibility = View.INVISIBLE
    }
}