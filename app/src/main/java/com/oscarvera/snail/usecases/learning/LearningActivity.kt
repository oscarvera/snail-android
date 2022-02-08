package com.oscarvera.snail.usecases.learning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.oscarvera.snail.R
import kotlinx.android.synthetic.main.activity_learning.*

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

        learningVM.card.observe(this, Observer {

            var card_data = it.cardData
            var data = card_data!!.first().text
            var data2 = card_data[1].text

            text_data_1.text = data
            text_data_2.text = data2


        })

        idDesk?.let { idDesk ->
            learningVM.getCards(idDesk)
        }

        btn_good_answer.setOnClickListener {
            learningVM.setResultCard(true)
        }

        btn_bad_answer.setOnClickListener {
            learningVM.setResultCard(false)
        }


        btn_back.setOnClickListener {
            finish()
        }


    }
}