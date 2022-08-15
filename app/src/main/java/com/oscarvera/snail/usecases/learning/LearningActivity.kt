package com.oscarvera.snail.usecases.learning

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.oscarvera.snail.R
import com.oscarvera.snail.databinding.ActivityLearningBinding

class LearningActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID_DESK = "idDesk"
        const val EXTRA_NAME_DESK = "nameDesk"
    }

    lateinit var learningVM: LearningViewModel

    private lateinit var binding: ActivityLearningBinding

    private var idDesk: String? = null
    private var nameDesk: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        learningVM = ViewModelProvider(this)[LearningViewModel::class.java]

        idDesk = intent.getStringExtra(EXTRA_ID_DESK)
        nameDesk = intent.getStringExtra(EXTRA_NAME_DESK)
        binding.layoutTopbar.titleTopBar.text = nameDesk

        binding.cardText1.movementMethod = ScrollingMovementMethod()
        binding.cardText2.movementMethod = ScrollingMovementMethod()

        learningVM.card.observe(this, Observer {

            resetAnswerView()

            val card_data = it.cardData
            val data = card_data!!.first().text
            val data2 = card_data[1].text

            binding.cardText1.text = data
            binding.cardText2.text = data2

        })

        learningVM.noMoreCards.observe(this, Observer {
            noMoreCards()

        })



        idDesk?.let { idDesk ->
            learningVM.getCards(idDesk)
        }

        binding.btnShow.setOnClickListener {
            showAnswerView()
        }

        binding.btnLearned.setOnClickListener {
            if (binding.btnNotLearned.visibility == View.VISIBLE) {
                //Card learned
                learningVM.setResultCard(true)
            } else {
                //Show answer
                showAnswerView()
            }

        }

        binding.btnNotLearned.setOnClickListener {
            learningVM.setResultCard(false)
        }

        binding.layoutTopbar.btnBack.setOnClickListener {
            finish()
        }


    }

    private fun resetAnswerView() {
        binding.btnNotLearned.visibility = View.INVISIBLE
        binding.imCheck.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_arrow_bottom))
        binding.cardText2.visibility = View.GONE
        binding.btnShow.visibility = View.VISIBLE
    }

    private fun showAnswerView() {
        binding.btnNotLearned.visibility = View.VISIBLE
        binding.imCheck.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_check))
        binding.cardText2.visibility = View.VISIBLE
        binding.btnShow.visibility = View.GONE
    }

    private fun noMoreCards() {
        binding.btnNotLearned.visibility = View.INVISIBLE
        binding.btnLearned.visibility = View.INVISIBLE
    }
}