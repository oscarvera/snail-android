package com.oscarvera.snail.usecases.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.model.domain.StatusCard
import com.oscarvera.snail.util.Utils


class DesksToCheckAdapter(
    listWithCards: List<DeskWithCards>,
    callback: DeskToCheckAdapterCallback
) :
    RecyclerView.Adapter<DesksToCheckAdapter.ViewHolderToLearn>() {


    private var listDesksCards = listWithCards
    private var callback = callback


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderToLearn =
        ViewHolderToLearn(
            LayoutInflater.from(parent.context).inflate(R.layout.item_desk_tolearn, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolderToLearn, position: Int) {
        val desk = listDesksCards[position]

        holder.name.text = desk.desk.name


        desk.cards?.let {

            val countStatesCards = Utils.countStatusCards(desk.cards)
            holder.textCardsLearned.text =
                "${countStatesCards[StatusCard.LEARNED]} ${holder.textCardsLearned.context.getString(R.string.name_card)}"
            holder.textCardsNumber.text =
                "${desk.cards.size} ${holder.textCardsLearned.context.getString(R.string.name_card)}"
            holder.textCardsToLearn.text =
                "${countStatesCards[StatusCard.TO_LEARN]} ${holder.textCardsLearned.context.getString(R.string.name_card)}"
            holder.numberToLearn.text = countStatesCards[StatusCard.TO_LEARN].toString()
            holder.textLastDay.text = " "

        } ?: {

            //TODO:No tiene cards

        }

        holder.card.setOnClickListener {
            callback.onClick(desk = desk)
        }


    }

    override fun getItemCount(): Int {
        return listDesksCards.size
    }

    inner class ViewHolderToLearn(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var numberToLearn: TextView
        var textCardsNumber: TextView
        var textCardsToLearn: TextView
        var textCardsLearned: TextView
        var textLastDay: TextView
        var card: CardView

        init {
            name = itemView.findViewById(R.id.item_desk_tolearn_name)
            numberToLearn = itemView.findViewById(R.id.text_number_tolearn)
            textCardsNumber = itemView.findViewById(R.id.text_numcards)
            textCardsToLearn = itemView.findViewById(R.id.text_to_learn)
            textCardsLearned = itemView.findViewById(R.id.text_learned)
            textLastDay = itemView.findViewById(R.id.text_lastday)
            card = itemView.findViewById(R.id.card_item)
        }
    }

    interface DeskToCheckAdapterCallback {

        fun onClick(desk: DeskWithCards)

    }

}