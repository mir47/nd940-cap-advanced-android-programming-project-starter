package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.DataRepository
import com.example.android.politicalpreparedness.repository.Result.Success
import com.example.android.politicalpreparedness.repository.succeeded
import kotlinx.coroutines.launch

class ElectionsViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _elections = MutableLiveData<List<Election>>()
    val elections: LiveData<List<Election>> = _elections

    val savedElections: LiveData<List<Election>> = dataRepository.savedElections()

    init {
        fetchElections()
    }

    private fun fetchElections() {
        viewModelScope.launch {
            val result = dataRepository.getElections()
            if (result.succeeded) {
                result as Success
                _elections.postValue(result.data)
            }
        }
    }
}