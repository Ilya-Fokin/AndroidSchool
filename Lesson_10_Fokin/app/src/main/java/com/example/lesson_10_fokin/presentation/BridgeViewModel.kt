package com.example.lesson_10_fokin.presentation

import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lesson_10_fokin.data.ApiClient
import com.example.lesson_10_fokin.data.LoadState
import com.example.lesson_10_fokin.data.model.Bridge
import kotlinx.coroutines.launch

class BridgeViewModel : ViewModel() {
    private val _bridgeLiveData = MutableLiveData<LoadState<List<Bridge>>>()

    val bridgeLiveData: LiveData<LoadState<List<Bridge>>> = _bridgeLiveData

    fun loadBridges() {
        viewModelScope.launch {
            _bridgeLiveData.postValue(LoadState.Loading())
            try {
                val bridges = ApiClient.apiService.getBridges()
                _bridgeLiveData.postValue(LoadState.Data(bridges))
            } catch (e: Exception) {
                _bridgeLiveData.postValue(LoadState.Error(e))
            }
        }
    }
}