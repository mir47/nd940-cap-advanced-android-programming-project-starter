package com.example.android.politicalpreparedness.repository

import android.util.Log
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DefaultElectionRepository(
    private val remoteDataSource: CivicsApiService,
    private val localDataSource: ElectionDao
) : ElectionRepository {

    override suspend fun getElections() {
        coroutineScope {
            launch {
                val e = remoteDataSource.getElections()
                Log.d(TAG, "getElections: $e")
            }
        }
    }

    override suspend fun saveElection(election: Election) {
        localDataSource.insertElection(election)
    }

    companion object {
        private const val TAG = "DefaultElectionRepository"
    }
}