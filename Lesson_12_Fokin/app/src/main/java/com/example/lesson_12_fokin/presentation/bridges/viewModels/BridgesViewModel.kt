package com.example.lesson_12_fokin.presentation.bridges.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lesson_12_fokin.data.remote.LoadState
import com.example.lesson_12_fokin.data.repository.BridgesRepository
import com.example.lesson_12_fokin.presentation.bridges.Bridge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BridgesViewModel @Inject constructor(
    private val repository: BridgesRepository
) : ViewModel() {

    private val _bridgesLiveData = MutableLiveData<LoadState<List<Bridge>>>()
    val bridgesLiveData: LiveData<LoadState<List<Bridge>>> = _bridgesLiveData

    /** Загрузка мостов из API и передача в фрагмент */
    fun loadBridges() {
        viewModelScope.launch(Dispatchers.IO) {
            _bridgesLiveData.postValue(LoadState.Loading())
            try {
                val bridges = repository.getBridges()
                _bridgesLiveData.postValue(LoadState.Data(bridges))
            } catch (ex: Exception) {
                _bridgesLiveData.postValue(LoadState.Error(ex))
            }
        }
    }
}