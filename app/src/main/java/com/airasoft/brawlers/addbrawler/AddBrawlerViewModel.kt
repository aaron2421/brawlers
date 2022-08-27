package com.airasoft.brawlers.addbrawler

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.airasoft.brawlers.model.Brawler
import com.airasoft.brawlers.database.BrawlersDatabaseDao
import com.airasoft.brawlers.model.SnackbarModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class AddBrawlerViewModel(
    private val dataSource: BrawlersDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val _brawler = MutableLiveData<Brawler?>()
    val brawler: LiveData<Brawler?>
        get() = _brawler

    fun setBrawlerData(brawler: Brawler) {
        _brawler.value = brawler
    }

    // Navigation config
    private val _navigateToListBrawlers = MutableLiveData<Boolean?>()
    val navigateToListBrawler: LiveData<Boolean?>
        get() = _navigateToListBrawlers

    fun doneNavigating() {
        _navigateToListBrawlers.value = null
    }

    // Snackbar configuration
    private var _showSnackBarEvent = MutableLiveData<SnackbarModel?>()
    val showSnackBarEvent: LiveData<SnackbarModel?>
        get() = _showSnackBarEvent

    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = SnackbarModel(false, "")
    }

    fun onCreateBrawler(brawler: Brawler) {
        createBrawler(brawler)
    }

    private fun createBrawler(brawler: Brawler) {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.insert(brawler)
        }
        _navigateToListBrawlers.value = true
        _showSnackBarEvent.value = SnackbarModel(true, "Brawler creado.")
    }

    fun onEditBrawler(brawler: Brawler) {
        editBrawler(brawler)
    }

    private fun editBrawler(brawler: Brawler) {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.update(brawler)
        }
        _showSnackBarEvent.value = SnackbarModel(true, "Brawler editado.")
        _navigateToListBrawlers.value = true
    }

    fun onClearInputs() {
        clearInputs()
    }

    private fun clearInputs() {
        _brawler.value = Brawler()
        _showSnackBarEvent.value = SnackbarModel(true, "Inputs limpios.")
    }
}