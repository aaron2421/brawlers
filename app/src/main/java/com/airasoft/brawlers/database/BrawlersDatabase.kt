package com.airasoft.brawlers.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.airasoft.brawlers.model.Brawler

@Database(entities = [Brawler::class], version = 3, exportSchema = false)
abstract class BrawlersDatabase: RoomDatabase() {

    abstract val brawlersDatabaseDao: BrawlersDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: BrawlersDatabase? = null

        fun getInstance(context: Context) : BrawlersDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BrawlersDatabase::class.java,
                        "brawlers_database"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}