package com.airasoft.brawlers.model

data class AlertDialogModel(
    val show: Boolean =  false, val message: String = "", val negativeButton: String = "", val positiveButton: String = "", val data: Any? = null
)
