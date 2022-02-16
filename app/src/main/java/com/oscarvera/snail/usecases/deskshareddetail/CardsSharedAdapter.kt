package com.oscarvera.snail.usecases.deskshareddetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.Card


class CardsSharedAdapter(
    private var listCards: List<Card>
) :
    RecyclerView.Adapter<CardsSharedAdapter.CardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {

        return CardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_shared, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = listCards[position]

        holder.firtText.text = card.cardswithdata[0].text
        holder.secondText.text = card.cardswithdata[1].text


    }

    override fun getItemCount(): Int {
        return listCards.size
    }


    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var firtText: TextView = itemView.findViewById(R.id.card_firt_text)
        var secondText: TextView = itemView.findViewById(R.id.card_second_text)
    }


}

