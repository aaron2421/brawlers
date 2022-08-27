package com.airasoft.brawlers

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.airasoft.brawlers.database.BrawlersDatabase
import com.airasoft.brawlers.databinding.ActivityMainBinding
import com.airasoft.brawlers.listbrawlers.ListBrawlers
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datasource = BrawlersDatabase.getInstance(application).brawlersDatabaseDao

        // bottom nav view
        //val navView: BottomNavigationView = binding.bottomNavView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //bottom nav view
        //navView.setupWithNavController(navController)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.deleteAllBrawlers -> {
                    MaterialAlertDialogBuilder(this)
                        .setMessage("Se eliminarán todos los brawlers.")
                        .setNegativeButton("Cancelar") { _, _ -> }
                        .setPositiveButton("Eliminar") { _, _ ->
                            datasource.deleteAllBrawlers()
                            Snackbar.make(this.findViewById(android.R.id.content), "Brawlers eliminados.", Snackbar.LENGTH_SHORT).show()
                        }
                        .show()
                    true
                }
                else -> false
            }
        }

        binding.topAppBar.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
