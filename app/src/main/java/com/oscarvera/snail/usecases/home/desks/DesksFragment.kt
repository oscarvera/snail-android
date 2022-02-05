package com.oscarvera.snail.usecases.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.Card
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.provider.CardDataSource
import com.oscarvera.snail.usecases.deskdetail.DeskDetailActivity
import com.oscarvera.snail.usecases.deskdetail.DeskDetailViewModel
import com.oscarvera.snail.usecases.home.desks.DesksViewModel
import com.oscarvera.snail.util.EventType
import com.oscarvera.snail.util.SendEvent
import com.oscarvera.snail.util.extensions.getProperId
import kotlinx.android.synthetic.main.fragment_home2.view.*


class DesksFragment : Fragment() {

    private var layoutManager2: RecyclerView.LayoutManager? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterToLearn: DesksToCheckAdapter? = null
    private var adapterAll: DesksCheckedAdapter? = null

    lateinit var desksViewModel: DesksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SendEvent(EventType.SHOWDESKVIEW, null)
        desksViewModel = ViewModelProvider(this).get(DesksViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home2, container, false)

        layoutManager2 = LinearLayoutManager(activity)
        view.listDesksToLearn.layoutManager = layoutManager2
        /*adapterToLearn = DesksToCheckAdapter(testDesksToLearnList, DesksToCheckAdapter.TYPE_ADAPTER.TYPE_TOLEARN)
        view.listDesksToLearn.adapter = adapterToLearn*/

        layoutManager = LinearLayoutManager(activity)
        view.listDesksAll.layoutManager = layoutManager

        desksViewModel.desksChecked.observe(viewLifecycleOwner, Observer {
            adapterAll = DesksCheckedAdapter(it,  object : DesksCheckedAdapter.DeskCheckedAdapterCallback {
                override fun onClick(desk: DeskWithCards) {
                    val intent = Intent(context, DeskDetailActivity::class.java)
                    intent.putExtra(DeskDetailActivity.EXTRA_ID_DESK, desk.getProperId())
                    startActivity(intent)
                }

            })
            view.listDesksAll.adapter = adapterAll
        })

        desksViewModel.desksToCheck.observe(viewLifecycleOwner, Observer {
            adapterToLearn = DesksToCheckAdapter(it,  object : DesksToCheckAdapter.DeskToCheckAdapterCallback {
                override fun onClick(desk: DeskWithCards) {
                    val intent = Intent(context, DeskDetailActivity::class.java)
                    intent.putExtra(DeskDetailActivity.EXTRA_ID_DESK, desk.getProperId())
                    startActivity(intent)
                }

            })
            view.listDesksToLearn.adapter = adapterToLearn
        })

        desksViewModel.getDeskWithCards()

        return view
    }
}