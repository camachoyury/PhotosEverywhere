package com.camachoyury.photoseverywhere.util

import android.content.Context
import java.io.File


class FileCache(context: Context) {
    private var cacheDir: File? = null
    fun getFile(url: String): File {
        val filename = url.hashCode().toString()
        return File(cacheDir, filename)
    }

    fun clear() {
        val files = cacheDir!!.listFiles() ?: return
        for (f in files) f.delete()
    }

    init {

         cacheDir = File(context.filesDir, "mydir")
        if (!cacheDir!!.exists()) {
            cacheDir!!.mkdir()
        }
    }



}