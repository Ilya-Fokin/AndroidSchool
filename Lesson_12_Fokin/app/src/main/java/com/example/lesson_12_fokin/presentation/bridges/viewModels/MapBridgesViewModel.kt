package com.example.lesson_12_fokin.presentation.bridges.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lesson_12_fokin.data.remote.LoadState
import com.example.lesson_12_fokin.data.repository.BridgesRepository
import com.example.lesson_12_fokin.data.remote.model.Bridge
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapBridgesViewModel @Inject constructor(
    private val repository: BridgesRepository
) : ViewModel() {

    private val _bridgesLiveData = MutableLiveData<LoadState<List<Bridge>>>()
    val bridgesLiveData: LiveData<LoadState<List<Bridge>>> = _bridgesLiveData

    private var _pairCameraToBridgeLiveData = MutableLiveData<Pair<CameraPosition, Bridge>?>()
    val pairCameraToBridgeLiveData: LiveData<Pair<CameraPosition, Bridge>?> = _pairCameraToBridgeLiveData

    init {
        _pairCameraToBridgeLiveData.value = null
    }

    /** Загрузка мостов из API и передача в фрагмент */
    fun loadBridges() {
        viewModelScope.launch {
            _bridgesLiveData.postValue(LoadState.Loading())
            try {
                val bridges = repository.getBridges()
                _bridgesLiveData.postValue(LoadState.Data(bridges))
            } catch (ex: Exception) {
                _bridgesLiveData.postValue(LoadState.Error(ex))
            }
        }
    }

    fun setBridge(bridge: Bridge, cameraPosition: CameraPosition) {
        _pairCameraToBridgeLiveData.value = Pair(cameraPosition, bridge)
    }
}