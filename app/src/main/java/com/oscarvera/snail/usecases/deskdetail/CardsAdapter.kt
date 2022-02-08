package com.oscarvera.snail.usecases.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData
import com.oscarvera.snail.util.extensions.getStatusName

enum class VIEWTYPE {
    ADD_VIEW,
    CARD_VIEW
}

class CardsAdapter(
    private var listCards: List<CardWithData>,
    private var callback: CardAdapterCallback
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEWTYPE.CARD_VIEW.ordinal -> CardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
            )
            VIEWTYPE.ADD_VIEW.ordinal -> AddCardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_new_card, parent, false)
            )
            else -> CardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEWTYPE.ADD_VIEW.ordinal
        } else {
            VIEWTYPE.CARD_VIEW.ordinal
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is CardViewHolder -> {
                val card = listCards[position - 1]

                card.cardData?.let { data ->
                    holder.title.text = data.first().text
                    holder.type.text = card.card.getStatusName(holder.itemView.context)
                }
            }

            is AddCardViewHolder -> {

                holder.card.setOnClickListener {

                    callback.onClickAddCard()

                }

            }


        }


    }

    override fun getItemCount(): Int {
        return listCards.size + 1
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.card_firt_text)
        var type: TextView = itemView.findViewById(R.id.card_type_text)
    }

    inner class AddCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card : CardView = itemView.findViewById(R.id.item_add_card)
    }

    interface CardAdapterCallback {

        fun onClick(card: Card)
        fun onClickAddCard()

    }


}

