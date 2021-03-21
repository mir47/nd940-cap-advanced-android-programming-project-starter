package com.example.android.politicalpreparedness.repository

import android.util.Log
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DefaultElectionRepository : ElectionRepository {

    override suspend fun getElections() {
        coroutineScope {
            launch {
                val e = CivicsApi.retrofitService.getElections()
                Log.d(TAG, "getElections: $e")
            }
        }
    }

    companion object {
        private const val TAG = "DefaultElectionRepository"
    }
}