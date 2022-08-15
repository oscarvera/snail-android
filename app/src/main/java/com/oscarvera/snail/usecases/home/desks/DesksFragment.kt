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
import com.oscarvera.snail.databinding.FragmentHomeBinding
import com.oscarvera.snail.model.domain.DeskWithCards
import com.oscarvera.snail.util.Dialogs
import com.oscarvera.snail.util.EventType
import com.oscarvera.snail.util.Router
import com.oscarvera.snail.util.sendEvent
import com.oscarvera.snail.util.extensions.getProperId


class DesksFragment : Fragment() {

    private var layoutManager2: RecyclerView.LayoutManager? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterToLearn: DesksToCheckAdapter? = null
    private var adapterAll: DesksCheckedAdapter? = null

    lateinit var desksViewModel: DesksViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendEvent(EventType.SHOWDESKSVIEW, null)
        desksViewModel = ViewModelProvider(this)[DesksViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        layoutManager2 = LinearLayoutManager(activity)
        binding.listDesksToLearn.layoutManager = layoutManager2

        layoutManager = LinearLayoutManager(activity)
        binding.listDesksAll.layoutManager = layoutManager

        desksViewModel.desksChecked.observe(viewLifecycleOwner, Observer {
            adapterAll =
                DesksCheckedAdapter(it, object : DesksCheckedAdapter.DeskCheckedAdapterCallback {
                    override fun onClick(desk: DeskWithCards) {

                        Router.launchDeskDetailActivity(context, desk.getProperId())

                    }

                })
            binding.listDesksAll.adapter = adapterAll
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
            binding.listDesksToLearn.adapter = adapterToLearn
        })

        desksViewModel.noDesks.observe(viewLifecycleOwner, Observer {
            if (it) {
                showNoDesksScreen(binding)
            } else {
                showDesksScreen(binding)
            }
        })

        binding.btnAddDesk.setOnClickListener {

            showAddDeskDialog()

        }
        return view
    }

    private fun showAddDeskDialog() {

        sendEvent(EventType.CLICKADDDESK,null)
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

    private fun showNoDesksScreen(view: FragmentHomeBinding) {

        view.titleSeparator1.visibility = View.GONE
        view.listDesksToLearn.visibility = View.GONE
        view.titleSeparator2.visibility = View.GONE
        view.listDesksAll.visibility = View.GONE

        view.animationNoDesk.visibility = View.VISIBLE

        view.animationNoDesk.playAnimation()

        view.animationNoDesk.setOnClickListener {
            showAddDeskDialog()
        }

    }

    private fun showDesksScreen(view: FragmentHomeBinding) {
        if (view.titleSeparator1.visibility == View.GONE) {
            view.titleSeparator1.visibility = View.VISIBLE
            view.listDesksToLearn.visibility = View.VISIBLE
            view.titleSeparator2.visibility = View.VISIBLE
            view.listDesksAll.visibility = View.VISIBLE
            view.animationNoDesk.visibility = View.GONE
        }
    }


    override fun onResume() {
        super.onResume()
        desksViewModel.getDeskWithCards()
    }
}