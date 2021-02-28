package com.camachoyury.photoseverywhere.data.entities

import com.google.gson.annotations.SerializedName

data class PhotosResponse (
    @SerializedName("results") val results : List<Photo>,
    @SerializedName("info") val info : Info
)