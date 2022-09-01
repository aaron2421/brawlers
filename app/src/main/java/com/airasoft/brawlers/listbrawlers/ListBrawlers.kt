package com.airasoft.brawlers.listbrawlers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.airasoft.brawlers.database.BrawlersDatabase
import com.airasoft.brawlers.database.BrawlersRepository
import com.airasoft.brawlers.databinding.ListBrawlersFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class ListBrawlers : Fragment() {
    private lateinit var binding: ListBrawlersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListBrawlersFragmentBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = BrawlersDatabase.getInstance(application).brawlersDatabaseDao
        val repository = BrawlersRepository(datasource)

        val viewModelFactory = ListBrawlersViewModelFactory(repository, application)
        val listBrawlersViewModel = ViewModelProvider(this, viewModelFactory)[ListBrawlersViewModel::class.java]

        binding.listBrawlersViewModel = listBrawlersViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // This code is to show recycler in grid
        val manager = GridLayoutManager(activity, 1)
        binding.brawlerList.layoutManager = manager

        val adapter = BrawlerAdapter(BrawlerListener { brawler ->
            listBrawlersViewModel.onBrawlerClicked(brawler)
        }, listBrawlersViewModel)

        // This line is to show recycler in linear
        //binding.brawlerList.layoutManager = LinearLayoutManager(context)

        binding.brawlerList.adapter = adapter

        val floatingButton = binding.addBrawlerFloating
        val nestedScrollView = binding.nestedScrollView

        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            // the delay of the extension of the FAB is set for 15 items
            if (scrollY > oldScrollY + 15 && floatingButton.isShown) {
                floatingButton.hide();
            }

            // the delay of the extension of the FAB is set for 15 items
            if (scrollY < oldScrollY - 15 && !floatingButton.isShown) {
                floatingButton.show();
            }

            // if the nestedScrollView is at the first item of the list then the
            // floating action should be in show state
            if (scrollY == 0) {
                floatingButton.show();
            }
        })

        listBrawlersViewModel.navigateToAddBrawler.observe(viewLifecycleOwner) { brawler ->
            brawler?.let {
                this.findNavController().navigate(ListBrawlersDirections.actionNavigationListBrawlersToNavigationAddBrawler(brawler))
                listBrawlersViewModel.onAddBrawlerNavigated()
            }
        }

        listBrawlersViewModel.navigateToDetailsBrawler.observe(viewLifecycleOwner) { brawler ->
            brawler?.let {
                this.findNavController().navigate(ListBrawlersDirections.actionNavigationListBrawlersToNavigationDetailsBrawler(brawler))
                listBrawlersViewModel.onBrawlerDetailsNavigated()
            }
        }

        listBrawlersViewModel.brawlers.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
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
                        listBrawlersViewModel.deleteBrawlerOptionSelected(it.data as Long)
                    }
                    .show()
                listBrawlersViewModel.doneShowingAlertDialog()
            }
        }

        return binding.root
    }

}