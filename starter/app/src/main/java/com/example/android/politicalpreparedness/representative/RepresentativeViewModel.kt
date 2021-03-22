package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.repository.Result
import com.example.android.politicalpreparedness.repository.succeeded
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val electionRepository: ElectionRepository) : ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives

    var address = MutableLiveData(Address())

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean> = _showProgress

    private val _checkLocation = MutableLiveData<Boolean>()
    val checkLocation: LiveData<Boolean> = _checkLocation

    fun onUseLocationClick() {
        _checkLocation.value = true
    }

    fun doneLocationCheck() {
        _checkLocation.value = false
    }

    fun loadRepresentatives() {
        _showProgress.value = true
        viewModelScope.launch {
            address.value?.toFormattedString()?.let {
                val result = electionRepository.getRepresentatives(it)
                if (result.succeeded) {
                    _showProgress.value = false
                    result as Result.Success
                    val offices = result.data.offices
                    val officials = result.data.officials
                    _representatives.value = offices.flatMap { office ->
                        office.getRepresentatives(officials)
                    }
                }
            }
        }
    }
}
