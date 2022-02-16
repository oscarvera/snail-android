package com.oscarvera.snail.usecases.home.shared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.model.domain.StatusCard


class DesksSharedAdapter(
    private var listDesksShared: List<DeskShared>,
    private val callback: DesksSharedAdapterCallback
) :
    RecyclerView.Adapter<DesksSharedAdapter.ViewHolderShared>() {

    var listDesk : List<DeskShared> = listDesksShared

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderShared {

        return ViewHolderShared(
            LayoutInflater.from(parent.context).inflate(R.layout.item_desk_shared, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolderShared, position: Int) {


        val desk = listDesk[position]

        holder.name.text = desk.name

        holder.btnDownload.backgroundTintMode = null

        desk.cards?.let {

            val countStatesCards = it.size
            val textCardsNumber = "$countStatesCards ${
                holder.textCardsNumber.context.getString(
                    R.string.name_card
                )
            }"
            holder.textCardsNumber.text = textCardsNumber
        }

        holder.textCreatedBy.text = desk.userName
        holder.textDownloads.text = desk.timesDownloaded.toString()
        holder.textUploaded.text = desk.uploaded



        holder.btnDownload.setOnClickListener {
            callback.onClickDownload(desk)
        }

        holder.btnShow.setOnClickListener {
            callback.onClick(desk = desk)
        }

        holder.card.setOnClickListener {
            callback.onClick(desk = desk)
        }


    }

    override fun getItemCount(): Int {
        return listDesk.size
    }

    fun setChangeDesks(list : List<DeskShared>){
        this.listDesk = list
        notifyDataSetChanged()
    }

    inner class ViewHolderShared(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var textCreatedBy: TextView
        var textCardsNumber: TextView
        var textUploaded: TextView
        var textDownloads: TextView
        var card: CardView
        var btnDownload: Button
        var btnShow: Button

        init {
            name = itemView.findViewById(R.id.item_desk_shared_name)
            textCreatedBy = itemView.findViewById(R.id.text_created_by)
            textCardsNumber = itemView.findViewById(R.id.text_numcards)
            textUploaded = itemView.findViewById(R.id.text_uploaded)
            textDownloads = itemView.findViewById(R.id.text_downloads)

            card = itemView.findViewById(R.id.card_item)
            btnDownload = itemView.findViewById(R.id.btn_download)
            btnShow = itemView.findViewById(R.id.btn_show)
        }
    }


    interface DesksSharedAdapterCallback {

        fun onClick(desk: DeskShared)
        fun onClickDownload(desk: DeskShared)

    }


}