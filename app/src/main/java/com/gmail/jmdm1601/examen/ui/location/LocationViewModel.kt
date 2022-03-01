package com.gmail.jmdm1601.examen.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.jmdm1601.examen.data.model.LocationResponse
import com.gmail.jmdm1601.examen.data.remote.FirebaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor() : ViewModel() {
    private val _result = MutableLiveData<List<LocationResponse>>()

    val result: LiveData<List<LocationResponse>> = _result

    init {
        viewModelScope.launch {
            FirebaseService.getLocations().collect {
                _result.postValue(it)
            }
        }
    }
}