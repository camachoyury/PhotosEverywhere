package com.camachoyury.photoseverywhere.viewmodel

import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camachoyury.photoseverywhere.data.entities.Photo
import com.camachoyury.photoseverywhere.data.repository.PhotosRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(private val photosRepository: PhotosRepositoryImpl): ViewModel(){


    private val _photos: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)
    val photos: StateFlow<ScreenState> = _photos.asStateFlow()


    @ExperimentalCoroutinesApi
    fun load() = viewModelScope.launch {
        _photos.value = ScreenState.Loading
        photosRepository.getPhotos().collect { _photos.value = ScreenState.Success(it) }
    }

}

sealed class ScreenState {
    object Loading : ScreenState()
    class Success(val photos: List<Photo>) : ScreenState()
}