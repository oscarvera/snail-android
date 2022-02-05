package com.oscarvera.snail.usecases.home

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.CardWithData


class CardsAdapter(private var listCards: List<CardWithData>, private var callback: CardAdapterCallback) :
    RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder =
        CardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        )


    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = listCards[position]

        card.cardData?.let { data ->
            holder.title.text = data.first().text
        }

    }

    override fun getItemCount(): Int {
        return listCards.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView


        init {
            title = itemView.findViewById(R.id.card_firt_text)
        }
    }

    interface CardAdapterCallback{

        fun onClick(card: Card)

    }


}

