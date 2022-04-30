package com.airasoft.brawlers.listbrawlers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airasoft.brawlers.R

class ListBrawlers : Fragment() {

    companion object {
        fun newInstance() = ListBrawlers()
    }

    private lateinit var viewModel: ListBrawlersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_brawlers_fragment, container, false)
    }

}