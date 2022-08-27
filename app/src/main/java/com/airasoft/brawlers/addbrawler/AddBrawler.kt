package com.airasoft.brawlers.addbrawler

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airasoft.brawlers.R
import com.airasoft.brawlers.database.BrawlersDatabase
import com.airasoft.brawlers.databinding.ActivityMainBinding
import com.airasoft.brawlers.databinding.AddBrawlerFragmentBinding
import com.airasoft.brawlers.model.Brawler
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar

class AddBrawler : Fragment() {
    private lateinit var binding: AddBrawlerFragmentBinding
    private lateinit var addBrawlerViewModel: AddBrawlerViewModel

    private lateinit var arguments: AddBrawlerArgs
    private lateinit var filePath: Uri
    private lateinit var bitmap: Bitmap

    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { result ->
            result?.let {
                filePath = result
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source =
                        ImageDecoder.createSource(requireContext().contentResolver!!, filePath)
                    bitmap = ImageDecoder.decodeBitmap(source)
                }
                binding.addBrawlerImage.setImageBitmap(bitmap)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddBrawlerFragmentBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = BrawlersDatabase.getInstance(application).brawlersDatabaseDao
        arguments = AddBrawlerArgs.fromBundle(requireArguments())

        val viewModelFactory = AddBrawlerViewModelFactory(datasource, application)
        addBrawlerViewModel =
            ViewModelProvider(this, viewModelFactory)[AddBrawlerViewModel::class.java]

        binding.addBrawlerViewModel = addBrawlerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        filePath = arguments.brawler.brawlerImage.toUri()

        // Define si se va a editar o crear el brawler
        if (arguments.brawler.brawlerName != "") {
            binding.editBrawler.visibility = View.VISIBLE
            binding.createBrawler.visibility = View.GONE

            addBrawlerViewModel.setBrawlerData(arguments.brawler)
        }

        addBrawlerViewModel.navigateToListBrawler.observe(viewLifecycleOwner) {
            if (it == true) {
                this.findNavController()
                    .navigate(AddBrawlerDirections.actionNavigationAddBrawlerToNavigationListBrawlers())
                addBrawlerViewModel.doneNavigating()
            }
        }

        addBrawlerViewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it!!.show) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    it.text,
                    Snackbar.LENGTH_SHORT
                ).show()
                // Reset state to make sure the snackbar is only shown once, even if the device has a configuration change.
                addBrawlerViewModel.doneShowingSnackbar()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectImage.setOnClickListener {
            takePhoto.launch(arrayOf("image/*"))
        }

        binding.editBrawler.setOnClickListener {
            checkData(true)
        }

        binding.createBrawler.setOnClickListener {
            checkData(false)
        }
    }

    private fun checkData(isEditable: Boolean) {
        val bName = binding.nameEdit.text.toString()
        val bClass = binding.classesDropdown.text.toString()
        val bType = binding.typesDropdown.text.toString()
        val bHealth = binding.healthEdit.text.toString()
        val bSpeed = binding.speedEdit.text.toString()
        val bAtack = binding.atackEdit.text.toString()
        val bDamage = binding.damageEdit.text.toString()
        val bRange = binding.rangeEdit.text.toString()
        val bSuper = binding.superEdit.text.toString()

        if (bName.isEmpty() || bClass.isEmpty() || bType.isEmpty() || bHealth.isEmpty() || bSpeed.isEmpty()
            || bAtack.isEmpty() || bDamage.isEmpty() || bRange.isEmpty() || bSuper.isEmpty()
        ) {
            //
            Toast.makeText(context, "No dejes espacios vacios", Toast.LENGTH_SHORT).show()
        } else {
            setBrawlerData(
                bName,
                bClass,
                bType,
                bHealth,
                bSpeed,
                bAtack,
                bDamage,
                bRange,
                bSuper,
                isEditable
            )
        }
    }

    private fun setBrawlerData(
        brawlerName: String,
        brawlerClass: String,
        brawlerType: String,
        brawlerHealth: String,
        brawlerSpeed: String,
        brawlerAtack: String,
        brawlerDamage: String,
        brawlerRange: String,
        brawlerSuper: String,
        isEditable: Boolean
    ) {

        if (isEditable) {
            addBrawlerViewModel.onEditBrawler(
                Brawler(
                    brawlerName,
                    brawlerClass,
                    brawlerType,
                    brawlerHealth,
                    brawlerSpeed,
                    brawlerAtack,
                    brawlerDamage,
                    brawlerRange,
                    brawlerSuper,
                    filePath.toString(),
                    arguments.brawler.brawlerId
                )
            )
        } else {
            addBrawlerViewModel.onCreateBrawler(
                Brawler(
                    brawlerName,
                    brawlerClass,
                    brawlerType,
                    brawlerHealth,
                    brawlerSpeed,
                    brawlerAtack,
                    brawlerDamage,
                    brawlerRange,
                    brawlerSuper,
                    filePath.toString()
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val classes = resources.getStringArray(R.array.classes)
        val types = resources.getStringArray(R.array.types)

        val classesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, classes)
        binding.classesDropdown.setAdapter(classesAdapter)

        val typesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, types)
        binding.typesDropdown.setAdapter(typesAdapter)
    }

}