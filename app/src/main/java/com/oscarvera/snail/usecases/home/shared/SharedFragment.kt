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
import com.oscarvera.snail.databinding.FragmentSharedBinding
import com.oscarvera.snail.model.domain.DeskShared
import com.oscarvera.snail.util.customs.LoadingDialog
import com.oscarvera.snail.util.Router
import com.oscarvera.snail.util.customs.Result
import com.oscarvera.snail.util.extensions.afterTextChanged


class SharedFragment : Fragment() {

    lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentSharedBinding? = null
    private val binding get() = _binding!!

    private var adapterDeskShared: DesksSharedAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        activity?.let {
            loadingDialog = LoadingDialog(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_shared, container, false)

        _binding = FragmentSharedBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.animationSharedBackground.playAnimation()
        binding.btnShareDesk.setOnClickListener {
            Router.launchShareDeskActivity(context)
        }

        layoutManager = LinearLayoutManager(activity)
        binding.listDesksShared.layoutManager = layoutManager


        sharedViewModel.desksShared.observe(viewLifecycleOwner, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    loadingDialog?.finishLoadingDialog(true)
                    result.data?.let {
                        fillDataDesks(it)
                    }
                }
                Result.Status.ERROR -> {}
                Result.Status.LOADING -> loadingDialog?.showLoadingDialog()
            }

        })

        sharedViewModel.isDesksShared.observe(viewLifecycleOwner, Observer {

            loadingDialog?.finishLoadingDialog()
            sharedViewModel.getDeskShared()

        })

        sharedViewModel.getDeskShared()



        return view
    }

    fun fillDataDesks(list: List<DeskShared>) {

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
        binding.listDesksShared.adapter = adapterDeskShared

        binding.editTextSearchShared.afterTextChanged {

            val listFilter = list.filter { desk ->
                desk.name.contains(it, true)
            }
            adapterDeskShared?.setChangeDesks(listFilter)

        }


    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.getDeskShared()
    }

}