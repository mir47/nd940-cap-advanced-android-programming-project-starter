package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.succeeded
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val electionRepository: ElectionRepository) : ViewModel() {

    //TODO: Establish live data for representatives and address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

    fun loadRepresentatives(address: String) {
        viewModelScope.launch {
//            val e = CivicsApi.retrofitService.getRepresentatives(address)
//            Log.d(TAG, "getRepresentatives: $e")

            val result = electionRepository.getRepresentatives(address)
            if (result.succeeded) {
                result as Result.Success
                val offices = result.data.offices
                val officials = result.data.officials
                _representatives.value = offices.flatMap { office ->
                    office.getRepresentatives(officials)
                }
            }
        }
    }

    companion object {
        private const val TAG = "RepresentativeViewModel"
    }
}
