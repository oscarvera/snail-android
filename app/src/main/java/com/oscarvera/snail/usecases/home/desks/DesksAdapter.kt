package com.oscarvera.snail.usecases.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.Desk


class DesksAdapter(list: List<Desk>, type: TYPE_ADAPTER): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class TYPE_ADAPTER{
        TYPE_TOLEARN,
        TYPE_ALLDESKS
    }
    private var listDesks = list
    private var typeAdapter = type
    private var context = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(typeAdapter){
            TYPE_ADAPTER.TYPE_TOLEARN -> {
                ViewHolderToLearn(LayoutInflater.from(parent.context).inflate(R.layout.item_desk_tolearn, parent, false))
            }
            TYPE_ADAPTER.TYPE_ALLDESKS -> {
                ViewHolderAll(LayoutInflater.from(parent.context).inflate(R.layout.item_desk_all, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val desk = listDesks[position]

        when (holder){

            is ViewHolderToLearn -> {
                /*holder.name.text = desk.name
                holder.numberToLearn.text = desk.numberCardsToLearn.toString()
                holder.textCardsLearned.text = "${desk.numberCardsLearned.toString()} ${holder.textCardsLearned.context.getString(R.string.name_card)}"
                holder.textCardsNumber.text =  "${desk.numberTotalCards.toString()} ${holder.textCardsLearned.context.getString(R.string.name_card)}"
                holder.textCardsToLearn.text = "${desk.numberCardsToLearn.toString()} ${holder.textCardsLearned.context.getString(R.string.name_card)}"
                holder.textLastDay.text = desk.lastDayLearned*/
            }

            is ViewHolderAll -> {

                holder.name.text = desk.name

            }
        }


    }

    override fun getItemCount(): Int {
        return listDesks.size
    }

    inner class ViewHolderToLearn(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView
        var numberToLearn : TextView
        var textCardsNumber : TextView
        var textCardsToLearn : TextView
        var textCardsLearned : TextView
        var textLastDay : TextView

        init {
            name = itemView.findViewById(R.id.item_desk_tolearn_name)
            numberToLearn = itemView.findViewById(R.id.text_number_tolearn)
            textCardsNumber = itemView.findViewById(R.id.text_numcards)
            textCardsToLearn = itemView.findViewById(R.id.text_to_learn)
            textCardsLearned = itemView.findViewById(R.id.text_learned)
            textLastDay = itemView.findViewById(R.id.text_lastday)
        }
    }

    inner class ViewHolderAll(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView

        init {
            name = itemView.findViewById(R.id.item_desk_all_name)
        }
    }

}