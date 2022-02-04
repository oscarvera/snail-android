package com.oscarvera.snail.usecases.home

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
import com.oscarvera.snail.usecases.home.desks.DesksViewModel
import com.oscarvera.snail.util.EventType
import com.oscarvera.snail.util.SendEvent
import kotlinx.android.synthetic.main.fragment_home2.view.*


class DesksFragment : Fragment() {

    private var layoutManager2: RecyclerView.LayoutManager? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterToLearn: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private var adapterAll: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

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
        /*adapterToLearn = DesksAdapter(testDesksToLearnList, DesksAdapter.TYPE_ADAPTER.TYPE_TOLEARN)
        view.listDesksToLearn.adapter = adapterToLearn*/

        layoutManager = LinearLayoutManager(activity)
        view.listDesksAll.layoutManager = layoutManager

        /*desksViewModel.desks.observe(viewLifecycleOwner, Observer {
            adapterAll = DesksAdapter(it, DesksAdapter.TYPE_ADAPTER.TYPE_ALLDESKS)
            view.listDesksAll.adapter = adapterAll
        })

        desksViewModel.deskWithCards.observe(viewLifecycleOwner, Observer {

            Log.d("Debug","${it.size}")

        })*/

        desksViewModel.getDeskWithCards()

        return view
    }
}