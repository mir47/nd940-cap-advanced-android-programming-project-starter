package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.Result.Success
import com.example.android.politicalpreparedness.data.Result.Error
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

// TODO: rename to DefaultDataRepository
class DefaultElectionRepository(
    private val remoteDataSource: CivicsApiService,
    private val localDataSource: ElectionDao
) : ElectionRepository {

    override suspend fun getElections(): Result<List<Election>> =
        Success(remoteDataSource.getElections().elections)

    override fun savedElections(): LiveData<List<Election>> =
        localDataSource.observeElections()

    override suspend fun getElectionById(electionId: Int): Result<Election> {
        localDataSource.getElectionById(electionId)?.let {
            return Success(it)
        }
        return Error(Exception())
    }

    override suspend fun getVouterInfo(address: String, electionId: Int): Result<VoterInfoResponse> =
        Success(remoteDataSource.getVoterInfo(address, electionId))

    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> =
        Success(remoteDataSource.getRepresentatives(address))

    override suspend fun saveElection(election: Election) =
        localDataSource.insertElection(election)

    override suspend fun deleteElection(electionId: Int) {
        localDataSource.deleteElectionById(electionId)
    }

    companion object {
        private const val TAG = "DefaultElectionRepository"
    }
}