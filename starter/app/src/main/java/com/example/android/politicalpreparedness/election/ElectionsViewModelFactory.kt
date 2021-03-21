package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.repository.ElectionRepository

@Suppress("UNCHECKED_CAST")
class ElectionsViewModelFactory(
    private val electionRepository: ElectionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (ElectionsViewModel(electionRepository) as T)
}