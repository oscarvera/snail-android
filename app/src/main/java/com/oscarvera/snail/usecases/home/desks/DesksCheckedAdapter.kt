package com.oscarvera.snail.usecases.home.desks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.DeskWithCards


class DesksCheckedAdapter(list: List<DeskWithCards>, callback: DeskCheckedAdapterCallback) :
    RecyclerView.Adapter<DesksCheckedAdapter.ViewHolderAll>() {

    private var listDesks = list
    private var callback = callback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAll =
        ViewHolderAll(
            LayoutInflater.from(parent.context).inflate(R.layout.item_desk_all, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolderAll, position: Int) {
        val desk = listDesks[position]

        holder.name.text = desk.desk.name

        val numCards = desk.cards?.size ?: 0
        holder.numCards.text = "$numCards ${holder.numCards.context.getString(R.string.name_card)}"
        holder.card.setOnClickListener {
            callback.onClick(desk = desk)
        }
    }

    override fun getItemCount(): Int {
        return listDesks.size
    }


    inner class ViewHolderAll(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var numCards: TextView
        var card: CardView

        init {
            name = itemView.findViewById(R.id.item_desk_all_name)
            card = itemView.findViewById(R.id.card_item)
            numCards = itemView.findViewById(R.id.text_numcards)
        }
    }

    interface DeskCheckedAdapterCallback {

        fun onClick(desk: DeskWithCards)

    }

}