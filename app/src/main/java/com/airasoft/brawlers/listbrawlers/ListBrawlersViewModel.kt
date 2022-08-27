package com.airasoft.brawlers.listbrawlers

import android.app.Application
import androidx.lifecycle.*
import com.airasoft.brawlers.model.Brawler
import com.airasoft.brawlers.database.BrawlersRepository
import com.airasoft.brawlers.model.AlertDialogModel
import com.airasoft.brawlers.model.SnackbarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ListBrawlersViewModel(
    private val repository: BrawlersRepository,
    application: Application
) : AndroidViewModel(application) {

    val brawlers = repository.getAllBrawlers().asLiveData()

    // Navigation config
    private val _navigateToAddBrawler = MutableLiveData<Brawler?>()
    val navigateToAddBrawler: LiveData<Brawler?>
        get() = _navigateToAddBrawler

    fun onAddBrawler() {
        _navigateToAddBrawler.value = Brawler()
    }

    fun onAddBrawlerNavigated() {
        _navigateToAddBrawler.value = null
    }

    private val _navigateToDetailsBrawler = MutableLiveData<Brawler?>()
    val navigateToDetailsBrawler
        get() = _navigateToDetailsBrawler

    fun onBrawlerClicked(brawler: Brawler) {
        _navigateToDetailsBrawler.value = brawler
    }

    fun onBrawlerDetailsNavigated() {
        _navigateToDetailsBrawler.value = null
    }

    // Snackbar configuration
    private var _showSnackBarEvent = MutableLiveData<SnackbarModel?>()
    val showSnackBarEvent: LiveData<SnackbarModel?>
        get() = _showSnackBarEvent

    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = SnackbarModel()
    }

    // Alert Dialog configuration
    private var _showAlertDialogEvent = MutableLiveData<AlertDialogModel?>()
    val showAlertDialogEvent: LiveData<AlertDialogModel?>
        get() = _showAlertDialogEvent

    fun doneShowingAlertDialog() {
        _showAlertDialogEvent.value = AlertDialogModel()
    }

    fun onEditBrawler(brawler: Brawler) {
        _navigateToAddBrawler.value = Brawler(
            brawler.brawlerName,
            brawler.brawlerClass,
            brawler.brawlerType,
            brawler.brawlerHealth,
            brawler.brawlerSpeed,
            brawler.brawlerAtack,
            brawler.brawlerDamage,
            brawler.brawlerRange,
            brawler.brawlerSuper,
            brawler.brawlerImage,
            brawler.brawlerId
        )
    }

    fun onDeleteBrawler(brawlerId: Long) {
        _showAlertDialogEvent.value =
            AlertDialogModel(true, "¿Desea eliminar el brawler?", "Cancelar", "Eliminar", brawlerId)
    }

    fun deleteBrawlerOptionSelected(brawlerId: Long) {
        deleteBrawler(brawlerId)
    }

    private fun deleteBrawler(brawlerId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBrawler(brawlerId)
        }
        _showSnackBarEvent.value = SnackbarModel(true, "Brawler eliminado.")
    }
}