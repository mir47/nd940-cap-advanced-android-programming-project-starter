package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

// TODO: rename to DataRepository
interface ElectionRepository {

    suspend fun getElections(): Result<List<Election>>

    fun savedElections(): LiveData<List<Election>>

    suspend fun getElectionById(electionId: Int): Result<Election>

    suspend fun getVouterInfo(address: String, electionId: Int): Result<VoterInfoResponse>

    suspend fun getRepresentatives(address: String): Result<RepresentativeResponse>

    suspend fun saveElection(election: Election)

    suspend fun deleteElection(electionId: Int)
}