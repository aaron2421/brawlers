package com.airasoft.brawlers.addbrawler

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airasoft.brawlers.R
import com.airasoft.brawlers.database.BrawlersDatabase
import com.airasoft.brawlers.databinding.AddBrawlerFragmentBinding
import com.airasoft.brawlers.model.Brawler
import com.google.android.material.snackbar.Snackbar

class AddBrawler : Fragment() {
    private lateinit var binding: AddBrawlerFragmentBinding
    private lateinit var addBrawlerViewModel: AddBrawlerViewModel

    private lateinit var arguments: AddBrawlerArgs
    private lateinit var filePath: Uri

    private val pickImage = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            filePath = uri
            binding.addBrawlerImage.setImageURI(filePath)
        } else {
            Log.i("addBrawlerImage", "Imagen no seleccionada.")
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

        filePath = arguments.brawler.brawlerImage.toUri()

        // Define si se va a editar o crear el brawler
        if (arguments.brawler.brawlerName != "") {
            binding.editBrawler.visibility = View.VISIBLE
            binding.createBrawler.visibility = View.GONE

            binding.nameEdit.setText(arguments.brawler.brawlerName)
            binding.classesDropdown.setText(arguments.brawler.brawlerClass)
            binding.typesDropdown.setText(arguments.brawler.brawlerType)
            binding.healthEdit.setText(arguments.brawler.brawlerHealth)
            binding.speedEdit.setText(arguments.brawler.brawlerSpeed)
            binding.atackEdit.setText(arguments.brawler.brawlerAtack)
            binding.damageEdit.setText(arguments.brawler.brawlerDamage)
            binding.rangeEdit.setText(arguments.brawler.brawlerRange)
            binding.superEdit.setText(arguments.brawler.brawlerSuper)
            binding.addBrawlerImage.setImageURI(filePath)
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
            pickImage.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        binding.clearInputs.setOnClickListener {
            onClearInputs()
        }

        binding.editBrawler.setOnClickListener {
            checkData(true)
        }

        binding.createBrawler.setOnClickListener {
            checkData(false)
        }
    }

    private fun onClearInputs() {
        binding.nameEdit.setText("")
        binding.classesDropdown.setText("")
        binding.typesDropdown.setText("")
        binding.healthEdit.setText("")
        binding.speedEdit.setText("")
        binding.atackEdit.setText("")
        binding.damageEdit.setText("")
        binding.rangeEdit.setText("")
        binding.superEdit.setText("")

       addBrawlerViewModel.onClearInputs()
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