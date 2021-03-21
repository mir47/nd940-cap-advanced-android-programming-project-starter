package com.example.android.politicalpreparedness

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.DefaultElectionRepository
import com.example.android.politicalpreparedness.repository.ElectionRepository

object ServiceLocator {

    private val lock = Any()

    private var database: ElectionDatabase? = null

    @Volatile
    var electionRepository: ElectionRepository? = null
        @VisibleForTesting set

    fun provideElectionRepository(context: Context): ElectionRepository {
        synchronized(this) {
            return electionRepository ?: createElectionRepository(context)
        }
    }

    private fun createElectionRepository(context: Context): ElectionRepository {
        val newRepo = DefaultElectionRepository(
            CivicsApi.retrofitService,
            ElectionDatabase.getInstance(context).electionDao
        )
        electionRepository = newRepo
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
            electionRepository = null
        }
    }
}