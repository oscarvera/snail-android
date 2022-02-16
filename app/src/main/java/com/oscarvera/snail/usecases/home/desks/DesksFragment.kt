package com.oscarvera.snail.usecases.home.desks

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oscarvera.snail.R
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.util.Dialogs
import com.oscarvera.snail.util.EventType
import com.oscarvera.snail.util.Router
import com.oscarvera.snail.util.SendEvent
import com.oscarvera.snail.util.extensions.getProperId
import kotlinx.android.synthetic.main.fragment_home.view.*


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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        layoutManager2 = LinearLayoutManager(activity)
        view.listDesksToLearn.layoutManager = layoutManager2

        layoutManager = LinearLayoutManager(activity)
        view.listDesksAll.layoutManager = layoutManager

        desksViewModel.desksChecked.observe(viewLifecycleOwner, Observer {
            adapterAll =
                DesksCheckedAdapter(it, object : DesksCheckedAdapter.DeskCheckedAdapterCallback {
                    override fun onClick(desk: DeskWithCards) {

                        Router.launchDeskDetailActivity(context, desk.getProperId())

                    }

                })
            view.listDesksAll.adapter = adapterAll
        })

        desksViewModel.desksToCheck.observe(viewLifecycleOwner, Observer {
            adapterToLearn =
                DesksToCheckAdapter(it, object : DesksToCheckAdapter.DeskToCheckAdapterCallback {
                    override fun onClick(desk: DeskWithCards) {

                        Router.launchDeskDetailActivity(context, desk.getProperId())

                    }

                    override fun onClickToLearn(desk: DeskWithCards) {
                        Router.launchLearningActivity(context, desk.getProperId(), desk.desk.name)
                    }

                })
            view.listDesksToLearn.adapter = adapterToLearn
        })

        desksViewModel.noDesks.observe(viewLifecycleOwner, Observer {
            if (it) {
                showNoDesksScreen(view)
            } else {
                showDesksScreen(view)
            }
        })

        view.btn_add_desk.setOnClickListener {

            showAddDeskDialog()

        }
        return view
    }

    private fun showAddDeskDialog() {

        activity?.let { fragActivity ->
            Dialogs.createNewDeskDialog(fragActivity, object : Dialogs.NewDeskDialog {
                override fun createDialog(name: String, dialog: Dialog) {
                    desksViewModel.addNewDesk(name) {
                        it?.let { newId ->
                            dialog.dismiss()
                            Router.launchDeskDetailActivity(fragActivity, newId)
                        }
                    }
                }
            })
        }

    }

    private fun showNoDesksScreen(view: View) {

        view.title_separator_1.visibility = View.GONE
        view.listDesksToLearn.visibility = View.GONE
        view.title_separator_2.visibility = View.GONE
        view.listDesksAll.visibility = View.GONE

        view.animation_noDesk.visibility = View.VISIBLE

        view.animation_noDesk.playAnimation()

        view.animation_noDesk.setOnClickListener {
            showAddDeskDialog()
        }

    }

    private fun showDesksScreen(view: View) {
        if (view.title_separator_1.visibility == View.GONE) {
            view.title_separator_1.visibility = View.VISIBLE
            view.listDesksToLearn.visibility = View.VISIBLE
            view.title_separator_2.visibility = View.VISIBLE
            view.listDesksAll.visibility = View.VISIBLE
            view.animation_noDesk.visibility = View.GONE
        }
    }


    override fun onResume() {
        super.onResume()
        desksViewModel.getDeskWithCards()
    }
}