package com.airasoft.brawlers.addbrawler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airasoft.brawlers.R

class AddBrawler : Fragment() {

    companion object {
        fun newInstance() = AddBrawler()
    }

    private lateinit var viewModel: AddBrawlerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_brawler_fragment, container, false)
    }

}