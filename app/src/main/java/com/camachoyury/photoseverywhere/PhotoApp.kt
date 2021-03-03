package com.camachoyury.photoseverywhere

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PhotoApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}