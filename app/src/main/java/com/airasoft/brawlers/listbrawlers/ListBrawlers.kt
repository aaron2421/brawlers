package com.airasoft.brawlers.listbrawlers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airasoft.brawlers.database.BrawlersDatabase
import com.airasoft.brawlers.database.BrawlersRepository
import com.airasoft.brawlers.databinding.ListBrawlersFragmentBinding
import com.airasoft.brawlers.model.Brawler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class ListBrawlers : Fragment() {
    private lateinit var binding: ListBrawlersFragmentBinding
    private lateinit var adapter: BrawlerAdapter
    private lateinit var listBrawlersViewModel: ListBrawlersViewModel
    private val linearLayout = LinearLayoutManager(context)
    private lateinit var brawlerMutableList: MutableList<Brawler>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListBrawlersFragmentBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = BrawlersDatabase.getInstance(application).brawlersDatabaseDao
        val repository = BrawlersRepository(datasource)

        val viewModelFactory = ListBrawlersViewModelFactory(repository, application)
        listBrawlersViewModel =
            ViewModelProvider(this, viewModelFactory)[ListBrawlersViewModel::class.java]

        brawlerMutableList = repository.getAllBrawlers().toMutableList()

        binding.etFilter.addTextChangedListener { query ->
            val filteredData = brawlerMutableList.filter { brawler ->
                brawler.brawlerName.lowercase().contains(query.toString().lowercase())
            }
            adapter.updateBrawlerList(filteredData)
        }

        // init recycler
        adapter = BrawlerAdapter(
            brawlerList = brawlerMutableList,
            onClickListener = { listBrawlersViewModel.onBrawlerClicked(it) },
            onClickEdit = { listBrawlersViewModel.onEditBrawler(it) },
            onClickDelete = { brawler ->
                listBrawlersViewModel.onDeleteBrawler(brawler)
            }
        )

        //val decoration = DividerItemDecoration(context, manager.orientation)
        binding.brawlerList.layoutManager = linearLayout
        binding.brawlerList.adapter = adapter
        //binding.brawlerList.addItemDecoration(decoration)

        val floatingButton = binding.addBrawlerFloating
        val nestedScrollView = binding.nestedScrollView

        floatingButton.setOnClickListener {
            listBrawlersViewModel.onAddBrawler()
        }

        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            // the delay of the extension of the FAB is set for 15 items
            if (scrollY > oldScrollY + 15 && floatingButton.isShown) {
                floatingButton.hide()
            }

            // the delay of the extension of the FAB is set for 15 items
            if (scrollY < oldScrollY - 15 && !floatingButton.isShown) {
                floatingButton.show()
            }

            // if the nestedScrollView is at the first item of the list then the
            // floating action should be in show state
            if (scrollY == 0) {
                floatingButton.show()
            }
        })

        listBrawlersViewModel.navigateToSelf.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    ListBrawlersDirections.actionNavigationListBrawlersSelf()
                )
                listBrawlersViewModel.doneSelfNavigating()
            }
        }

        listBrawlersViewModel.navigateToAddBrawler.observe(viewLifecycleOwner) { brawler ->
            brawler?.let {
                this.findNavController().navigate(
                    ListBrawlersDirections.actionNavigationListBrawlersToNavigationAddBrawler(
                        brawler
                    )
                )
                listBrawlersViewModel.onAddBrawlerNavigated()
            }
        }

        listBrawlersViewModel.navigateToDetailsBrawler.observe(viewLifecycleOwner) { brawler ->
            brawler?.let {
                this.findNavController().navigate(
                    ListBrawlersDirections.actionNavigationListBrawlersToNavigationDetailsBrawler(
                        brawler
                    )
                )
                listBrawlersViewModel.onBrawlerDetailsNavigated()
            }
        }

        listBrawlersViewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it!!.show) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    it.text,
                    Snackbar.LENGTH_SHORT
                ).show()
                // Reset state to make sure the snackbar is only shown once, even if the device has a configuration change.
                listBrawlersViewModel.doneShowingSnackbar()
            }
        }

        listBrawlersViewModel.showAlertDialogEvent.observe(viewLifecycleOwner) {
            if (it!!.show) {
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(it.message)
                    .setNegativeButton(it.negativeButton) { _, _ -> }
                    .setPositiveButton(it.positiveButton) { _, _ ->
                        listBrawlersViewModel.deleteBrawlerOptionSelected(it.data as Brawler)
                    }
                    .show()
                listBrawlersViewModel.doneShowingAlertDialog()
            }
        }

        return binding.root
    }

}