package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel : ViewModel() {

    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

    init {
        viewModelScope.launch {
            val e = CivicsApi.retrofitService.getElections()
            Log.d(TAG, "getElections: $e")
        }

//        viewModelScope.launch {
//            val e = CivicsApi.retrofitService.getVoterInfo("new york", 0)
//            Log.d(TAG, "getVoterInfo: $e")
//        }
    }

    companion object {
        private const val TAG = "ElectionsViewModel"
    }
}