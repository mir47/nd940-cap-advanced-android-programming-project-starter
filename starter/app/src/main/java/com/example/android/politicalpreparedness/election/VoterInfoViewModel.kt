package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.DataRepository
import com.example.android.politicalpreparedness.repository.Result
import com.example.android.politicalpreparedness.repository.succeeded
import kotlinx.coroutines.launch

enum class ElectionState { SAVED, NOT_SAVED }

class VoterInfoViewModel(
    private val dataRepository: DataRepository,
    private val election: Election
) : ViewModel() {

    private val _electionState = MutableLiveData<ElectionState>()
    val electionState: LiveData<ElectionState> = _electionState

    private val _openUrl = MutableLiveData<String>()
    val openUrl: LiveData<String> = _openUrl

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse> = _voterInfo

    init {
        fetchElection()
        fetchVouterInfo()
    }

    fun toggleElection() {
        viewModelScope.launch {
            if (ElectionState.SAVED == _electionState.value) {
                dataRepository.deleteElection(election.id)
                _electionState.value = ElectionState.NOT_SAVED
            } else {
                dataRepository.saveElection(election)
                _electionState.value = ElectionState.SAVED
            }
        }
    }

    fun openUrl(url: String) {
        _openUrl.value = url
    }

    private fun fetchElection() {
        viewModelScope.launch {
            val result = dataRepository.getElectionById(election.id)
            if (result.succeeded) {
                _electionState.value = ElectionState.SAVED
            } else {
                _electionState.value = ElectionState.NOT_SAVED
            }
        }
    }

    private fun fetchVouterInfo() {
        viewModelScope.launch {
            val dummyAddress = "Seattle"
            val result = dataRepository.getVouterInfo(dummyAddress, election.id)
            if (result.succeeded) {
                result as Result.Success
                _voterInfo.value = result.data
            }
        }
    }
}