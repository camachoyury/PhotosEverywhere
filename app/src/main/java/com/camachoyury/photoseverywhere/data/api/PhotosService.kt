package com.camachoyury.photoseverywhere.data.api

import com.camachoyury.photoseverywhere.data.entities.PhotosResponse
import retrofit2.http.GET

interface PhotosService {

    @GET("?results=10")
    suspend fun getPhotos(): PhotosResponse
}