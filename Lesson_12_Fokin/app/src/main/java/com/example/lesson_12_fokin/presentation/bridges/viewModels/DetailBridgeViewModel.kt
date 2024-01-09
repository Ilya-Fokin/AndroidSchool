package com.example.lesson_12_fokin.presentation.bridges.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lesson_12_fokin.data.remote.LoadState
import com.example.lesson_12_fokin.data.remote.model.ApiBridge
import com.example.lesson_12_fokin.data.repository.BridgesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailBridgeViewModel @Inject constructor(
    private val repository: BridgesRepository
) : ViewModel() {

    private val _bridgeLiveData = MutableLiveData<LoadState<ApiBridge>>()
    val bridgeLiveData: LiveData<LoadState<ApiBridge>> = _bridgeLiveData

    /** Загрузка моста по id из API и передача в фрагмент */
    fun loadBridgeById(id: Int) {
        viewModelScope.launch {
            _bridgeLiveData.postValue(LoadState.Loading())
            try {
                val bridge = repository.getBridgeById(id)
                _bridgeLiveData.postValue(LoadState.Data(bridge))
            } catch (ex: Exception) {
                _bridgeLiveData.postValue(LoadState.Error(ex))
            }
        }
    }
}