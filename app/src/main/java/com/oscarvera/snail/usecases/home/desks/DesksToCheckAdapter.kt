package com.oscarvera.snail.usecases.home.desks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.util.Utils


class DesksToCheckAdapter(
    listWithCards: List<DeskWithCards>,
    private val callback: DeskToCheckAdapterCallback
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var listDesksCards = listWithCards
    private var noDeskToLearn = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (noDeskToLearn){
            ViewHolderNoToLearn(
                LayoutInflater.from(parent.context).inflate(R.layout.item_desk_no_desk_learn, parent, false)
            )
        }else{
            ViewHolderToLearn(
                LayoutInflater.from(parent.context).inflate(R.layout.item_desk_tolearn, parent, false)
            )
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder){

            is ViewHolderToLearn ->{

                val desk = listDesksCards[position]

                holder.name.text = desk.desk.name

                holder.btnLearn.backgroundTintMode = null

                desk.cards?.let {

                    val countStatesCards = Utils.countStatusCards(desk.cards)
                    val textCountLearned =  "${countStatesCards[StatusCard.LEARNED]} ${
                        holder.textCardsLearned.context.getString(
                            R.string.name_card
                        )
                    }"
                    holder.textCardsLearned.text = textCountLearned

                    val textCardsNumber =  "${desk.cards.size} ${holder.textCardsLearned.context.getString(R.string.name_card)}"
                    holder.textCardsNumber.text = textCardsNumber

                    val textCountLearn = "${countStatesCards[StatusCard.TO_LEARN]} ${
                        holder.textCardsLearned.context.getString(
                            R.string.name_card
                        )
                    }"
                    holder.textCardsToLearn.text = textCountLearn

                    holder.numberToLearn.text = countStatesCards[StatusCard.TO_LEARN].toString()
                    holder.textLastDay.text = ""

                } ?: {

                    //TODO:No tiene cards

                }

                holder.btnLearn.setOnClickListener {
                    callback.onClickToLearn(desk)
                }

                holder.btnEdit.setOnClickListener {
                    callback.onClick(desk = desk)
                }

                holder.card.setOnClickListener {
                    callback.onClick(desk = desk)
                }

            }

        }

    }

    override fun getItemCount(): Int {
        return if (listDesksCards.isEmpty()) {
            noDeskToLearn = true
            1
        } else {
            listDesksCards.size
        }
    }

    inner class ViewHolderToLearn(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var numberToLearn: TextView
        var textCardsNumber: TextView
        var textCardsToLearn: TextView
        var textCardsLearned: TextView
        var textLastDay: TextView
        var card: CardView
        var btnLearn: Button
        var btnEdit: Button

        init {
            name = itemView.findViewById(R.id.item_desk_tolearn_name)
            numberToLearn = itemView.findViewById(R.id.text_number_tolearn)
            textCardsNumber = itemView.findViewById(R.id.text_numcards)
            textCardsToLearn = itemView.findViewById(R.id.text_to_learn)
            textCardsLearned = itemView.findViewById(R.id.text_learned)
            textLastDay = itemView.findViewById(R.id.text_lastday)
            card = itemView.findViewById(R.id.card_item)
            btnLearn = itemView.findViewById(R.id.btn_learn)
            btnEdit = itemView.findViewById(R.id.btn_edit)
        }
    }

    inner class ViewHolderNoToLearn(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface DeskToCheckAdapterCallback {

        fun onClick(desk: DeskWithCards)
        fun onClickToLearn(desk: DeskWithCards)

    }

}