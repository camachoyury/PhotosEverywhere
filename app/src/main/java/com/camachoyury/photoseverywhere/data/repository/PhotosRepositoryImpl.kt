package com.camachoyury.photoseverywhere.data.repository

import com.camachoyury.photoseverywhere.data.api.PhotosService
import com.camachoyury.photoseverywhere.data.entities.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class PhotosRepositoryImpl @Inject constructor (private val photosService: PhotosService) {

    fun getPhotos(): Flow<List<Photo>> {
        return flow {
            val photos = photosService.getPhotos().results
            emit(photos)
        }.flowOn(Dispatchers.IO)
    }

}