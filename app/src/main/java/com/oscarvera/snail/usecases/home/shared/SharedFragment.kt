package com.oscarvera.snail.usecases.home.shared

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
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.util.LoadingDialog
import com.oscarvera.snail.util.Router
import com.oscarvera.snail.util.extensions.afterTextChanged
import kotlinx.android.synthetic.main.fragment_shared.*
import kotlinx.android.synthetic.main.fragment_shared.view.*


class SharedFragment : Fragment() {

    lateinit var sharedViewModel: SharedViewModel

    private var adapterDeskShared: DesksSharedAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        activity?.let {
            loadingDialog = LoadingDialog(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shared, container, false)

        view.animation_shared_background.playAnimation()
        view.btn_share_desk.setOnClickListener {
            Router.launchShareDeskActivity(context)
        }

        layoutManager = LinearLayoutManager(activity)
        view.listDesksShared.layoutManager = layoutManager


        sharedViewModel.desksShared.observe(viewLifecycleOwner, Observer { list ->
            adapterDeskShared =
                DesksSharedAdapter(list, object : DesksSharedAdapter.DesksSharedAdapterCallback {

                    override fun onClick(desk: DeskShared) {
                        Router.launchDeskSharedDetailActivity(context, desk.idRemote)
                    }

                    override fun onClickDownload(desk: DeskShared) {
                        loadingDialog?.setCallback(object : LoadingDialog.LoadingDialogCallback {
                            override fun onFinish(dialog: Dialog) {
                                dialog.dismiss()
                            }
                        })
                        loadingDialog?.showLoadingDialog()
                        sharedViewModel.downloadDesk(desk)

                    }

                })
            view.listDesksShared.adapter = adapterDeskShared

            edit_text_search_shared.afterTextChanged {

                val listFilter = list.filter { desk ->
                    desk.name.contains(it, true)
                }
                adapterDeskShared?.setChangeDesks(listFilter)

            }

        })


        sharedViewModel.isDesksShared.observe(viewLifecycleOwner, Observer {

            loadingDialog?.finishLoadingDialog()
            sharedViewModel.getDeskShared()

        })

        sharedViewModel.getDeskShared()



        return view
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.getDeskShared()
    }

}