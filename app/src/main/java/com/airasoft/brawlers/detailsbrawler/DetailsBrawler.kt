package com.airasoft.brawlers.detailsbrawler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airasoft.brawlers.database.BrawlersDatabase
import com.airasoft.brawlers.databinding.DetailsBrawlerFragmentBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class DetailsBrawler : Fragment() {
    private lateinit var binding: DetailsBrawlerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailsBrawlerFragmentBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = DetailsBrawlerArgs.fromBundle(requireArguments())
        val dataSource = BrawlersDatabase.getInstance(application).brawlersDatabaseDao
        val viewModelFactory = DetailsBrawlerViewModelFactory(arguments.brawler, dataSource)

        val detailsBrawlerViewModel = ViewModelProvider(this, viewModelFactory)[DetailsBrawlerViewModel::class.java]

        val receivedBrawler = detailsBrawlerViewModel.brawler

        Glide.with(binding.detailsBrawlerImage.context).load(receivedBrawler.brawlerImage.toUri()).into(binding.detailsBrawlerImage)
        binding.detailsNameValue.text = receivedBrawler.brawlerName
        binding.detailsClassValue.text = receivedBrawler.brawlerClass
        binding.detailsTypeValue.text = receivedBrawler.brawlerType
        binding.detailsHealthValue.text = receivedBrawler.brawlerHealth
        binding.detailsSpeedValue.text = receivedBrawler.brawlerSpeed
        binding.detailsAtackValue.text = receivedBrawler.brawlerAtack
        binding.detailsSuperValue.text = receivedBrawler.brawlerSuper
        binding.detailsDamageValue.text = receivedBrawler.brawlerDamage
        binding.detailsRangeValue.text = receivedBrawler.brawlerRange


        detailsBrawlerViewModel.navigateToListBrawler.observe(viewLifecycleOwner) {
            if (it == true) {
                this.findNavController().navigate(DetailsBrawlerDirections.actionNavigationDetailsBrawlerToNavigationListBrawlers())
                detailsBrawlerViewModel.doneNavigating()
            }
        }

        detailsBrawlerViewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it!!.show) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    it.text,
                    Snackbar.LENGTH_SHORT
                ).show()
                // Reset state to make sure the snackbar is only shown once, even if the device has a configuration change.
                detailsBrawlerViewModel.doneShowingSnackbar()
            }
        }

        return binding.root
    }


}