package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.DataRepository

@Suppress("UNCHECKED_CAST")
class VoterInfoViewModelFactory(
    private val dataRepository: DataRepository,
    private val election: Election
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (VoterInfoViewModel(dataRepository, election) as T)
}