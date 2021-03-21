package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.Result.Success
import com.example.android.politicalpreparedness.data.succeeded
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch

class ElectionsViewModel(private val electionRepository: ElectionRepository) : ViewModel() {

    private val _elections = MutableLiveData<List<Election>>()
    val elections: LiveData<List<Election>> = _elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

    init {
//        viewModelScope.launch {
//            val e = CivicsApi.retrofitService.getVoterInfo("new york", 0)
//            Log.d(TAG, "getVoterInfo: $e")
//        }
    }

    fun fetchElections() {
        viewModelScope.launch {
            val result = electionRepository.getElections()
            if (result.succeeded) {
                result as Success
                Log.d(TAG, "getElections: ${result.data}")
                _elections.postValue(result.data)
            }
        }
    }

    companion object {
        private const val TAG = "ElectionsViewModel"
    }
}