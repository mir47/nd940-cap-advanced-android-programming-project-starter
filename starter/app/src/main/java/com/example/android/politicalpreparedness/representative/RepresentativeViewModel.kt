package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.DataRepository
import com.example.android.politicalpreparedness.repository.Result
import com.example.android.politicalpreparedness.repository.succeeded
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives

    var address = MutableLiveData(Address())

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean> = _showProgress

    private val _showError = MutableLiveData<Int?>()
    val showError: LiveData<Int?> = _showError

    private val _checkLocation = MutableLiveData<Boolean>()
    val checkLocation: LiveData<Boolean> = _checkLocation

    fun onUseLocationClick() {
        _checkLocation.value = true
    }

    fun doneLocationCheck() {
        _checkLocation.value = false
    }

    fun doneShowingError() {
        _showError.value = null
    }

    fun loadRepresentatives() {
        viewModelScope.launch {
            address.value?.toFormattedString()?.let {
                _showProgress.value = true
                val result = dataRepository.getRepresentatives(it)
                _showProgress.value = false
                if (result.succeeded) {
                    result as Result.Success
                    val offices = result.data.offices
                    val officials = result.data.officials
                    _representatives.value = offices.flatMap { office ->
                        office.getRepresentatives(officials)
                    }
                } else {
                    _showError.value = R.string.error_loading_reps
                }
            } ?: run {
                _showError.value = R.string.error_address
            }
        }
    }
}
