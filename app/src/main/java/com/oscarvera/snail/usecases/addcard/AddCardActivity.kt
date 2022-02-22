package com.oscarvera.snail.usecases.addcard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardData
import com.oscarvera.snail.util.Utils
import kotlinx.android.synthetic.main.activity_add_card.*
import kotlinx.android.synthetic.main.layout_top_bar.*

class AddCardActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID_DESK = "idDesk"
        const val EXTRA_NAME_DESK = "nameDesk"

    }

    lateinit var addCardVM: AddCardViewModel

    private var idDesk: String? = null
    private var nameDesk: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        addCardVM = ViewModelProvider(this).get(AddCardViewModel::class.java)

        idDesk = intent.getStringExtra(EXTRA_ID_DESK)
        nameDesk = intent.getStringExtra(EXTRA_NAME_DESK)

        addCardVM.isCardAddedSuccessfully.observe(this, Observer {

            if (it){
                /*Snackbar.make(
                    frame_container,
                    R.string.dialog_newdesk_action_added_success,
                    Snackbar.LENGTH_SHORT
                ).show()*/

                resetFields()

            }


        })

        nameDesk?.let {
            title_top_bar.text = it
        }

        idDesk?.let { idDesk ->

            btn_add_card.setOnClickListener {

                when {

                    edit_text_1.text.toString().isEmpty() -> {
                        edit_text_1.error = getString(R.string.add_card_screen_error_void_field)
                    }
                    edit_text_2.text.toString().isEmpty() -> {
                        edit_text_2.error = getString(R.string.add_card_screen_error_void_field)
                    }

                    else -> {
                        val firstText = edit_text_1.text.toString()
                        val secondText = edit_text_2.text.toString()
                        val cardToSend = Card()
                        cardToSend.date_added = Utils.getNowDateFormatted()
                        cardToSend.cardswithdata =
                            listOf(CardData(0, 0, firstText), CardData(0, 0, secondText))
                        addCardVM.addCard(idDesk, cardToSend)
                    }

                }

            }

        }




        btn_back.setOnClickListener {
            finish()
        }


    }

    private fun resetFields(){
        edit_text_1.text = null
        edit_text_2.text = null
    }
}