package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.Result.Success
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election

class DefaultElectionRepository(
    private val remoteDataSource: CivicsApiService,
    private val localDataSource: ElectionDao
) : ElectionRepository {

    override suspend fun getElections(): Result<List<Election>> {
        return Success(remoteDataSource.getElections().elections)
    }

    override suspend fun saveElection(election: Election) {
        localDataSource.insertElection(election)
    }

    companion object {
        private const val TAG = "DefaultElectionRepository"
    }
}