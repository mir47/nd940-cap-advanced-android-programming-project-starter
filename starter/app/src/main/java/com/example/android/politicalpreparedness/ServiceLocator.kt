package com.example.android.politicalpreparedness

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.DefaultDataRepository
import com.example.android.politicalpreparedness.repository.DataRepository

object ServiceLocator {

    private val lock = Any()

    private var database: ElectionDatabase? = null

    @Volatile
    var dataRepository: DataRepository? = null
        @VisibleForTesting set

    fun provideDataRepository(context: Context): DataRepository {
        synchronized(this) {
            return dataRepository ?: createDataRepository(context)
        }
    }

    private fun createDataRepository(context: Context): DataRepository {
        val newRepo = DefaultDataRepository(
            CivicsApi.retrofitService,
            ElectionDatabase.getInstance(context).electionDao
        )
        dataRepository = newRepo
        return newRepo
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            dataRepository = null
        }
    }
}