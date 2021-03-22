package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository

@Suppress("UNCHECKED_CAST")
class VoterInfoViewModelFactory(
    private val electionRepository: ElectionRepository,
    private val election: Election
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (VoterInfoViewModel(electionRepository, election) as T)
}