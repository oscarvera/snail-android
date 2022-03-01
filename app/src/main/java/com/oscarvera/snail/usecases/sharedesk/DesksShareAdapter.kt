package com.oscarvera.snail.usecases.sharedesk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.DeskWithCards


class DesksShareAdapter(list: List<DeskWithCards>, callback: DesksShareAdapter.DeskShareAdapterCallback) :
    RecyclerView.Adapter<DesksShareAdapter.ViewHolderAll>() {

    private var listDesks = list
    private var callback = callback
    private var positionChecked: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAll =
        ViewHolderAll(
            LayoutInflater.from(parent.context).inflate(R.layout.item_desk_to_share, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolderAll, position: Int) {
        val desk = listDesks[position]

        holder.name.text = desk.desk.name

        val numCards = desk.cards?.size ?: 0
        val textCardNumbers = "$numCards ${holder.numCards.context.getString(R.string.name_card)}"
        holder.numCards.text = textCardNumbers
        holder.card.setOnClickListener {
            callback.onClick(desk = desk, position)
        }

        if (position==positionChecked){
            holder.backgroundCheck.background = ResourcesCompat.getDrawable(holder.backgroundCheck.resources,R.drawable.circle_primary, null)
            holder.imageCheck.visibility = View.VISIBLE
        }else{
            holder.backgroundCheck.background = ResourcesCompat.getDrawable(holder.backgroundCheck.resources,R.drawable.circle_white, null)
            holder.imageCheck.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int {
        return listDesks.size
    }


    inner class ViewHolderAll(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var numCards: TextView
        var card: CardView
        var backgroundCheck: FrameLayout
        var imageCheck: ImageView

        init {
            name = itemView.findViewById(R.id.item_desk_share_name)
            card = itemView.findViewById(R.id.card_item)
            numCards = itemView.findViewById(R.id.text_numcards)
            backgroundCheck = itemView.findViewById(R.id.background_check)
            imageCheck = itemView.findViewById(R.id.image_check)
        }
    }

    fun setPositionChecked(position: Int){
        this.positionChecked = position
    }

    fun getPositionChecked() : Int?{
        return this.positionChecked
    }

    interface DeskShareAdapterCallback {

        fun onClick(desk: DeskWithCards, position: Int)

    }

}