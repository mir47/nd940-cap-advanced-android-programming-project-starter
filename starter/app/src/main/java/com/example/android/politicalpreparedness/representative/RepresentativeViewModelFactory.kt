package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.repository.ElectionRepository

@Suppress("UNCHECKED_CAST")
class RepresentativeViewModelFactory(
    private val electionRepository: ElectionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (RepresentativeViewModel(electionRepository) as T)
}