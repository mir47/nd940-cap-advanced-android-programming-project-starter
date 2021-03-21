package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.network.models.Election

interface ElectionRepository {

    suspend fun getElections(): Result<List<Election>>

    suspend fun saveElection(election: Election)
}