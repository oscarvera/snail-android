package com.oscarvera.snail.usecases.addcard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.oscarvera.snail.R
import com.oscarvera.snail.databinding.ActivityAddCardBinding
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardData
import com.oscarvera.snail.util.Utils

class AddCardActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID_DESK = "idDesk"
        const val EXTRA_NAME_DESK = "nameDesk"

    }

    private lateinit var addCardVM: AddCardViewModel
    private lateinit var binding: ActivityAddCardBinding

    private var idDesk: String? = null
    private var nameDesk: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        addCardVM = ViewModelProvider(this)[AddCardViewModel::class.java]

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
            binding.layoutTopbar.titleTopBar.text = it
        }

        idDesk?.let { idDesk ->

            binding.btnAddCard.setOnClickListener {

                when {

                    binding.editText1.text.toString().isEmpty() -> {
                        binding.editText1.error = getString(R.string.add_card_screen_error_void_field)
                    }
                    binding.editText2.text.toString().isEmpty() -> {
                        binding.editText2.error = getString(R.string.add_card_screen_error_void_field)
                    }

                    else -> {
                        val firstText = binding.editText1.text.toString()
                        val secondText = binding.editText2.text.toString()
                        val cardToSend = Card()
                        cardToSend.date_added = Utils.getNowDateFormatted()
                        cardToSend.cardswithdata =
                            listOf(CardData(0, 0, firstText), CardData(0, 0, secondText))
                        addCardVM.addCard(idDesk, cardToSend)
                    }

                }

            }

        }




        binding.layoutTopbar.btnBack.setOnClickListener {
            finish()
        }


    }

    private fun resetFields(){
        binding.editText1.text = null
        binding.editText2.text = null
    }
}