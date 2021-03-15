package com.camachoyury.photoseverywhere

import android.R.attr.data
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.camachoyury.photoseverywhere.data.entities.Photo
import com.camachoyury.photoseverywhere.databinding.ItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL


class PhotosAdapter(var photos: List<Photo>) :
    RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        with(holder) {
            with(photos[position]) {
                GlobalScope.launch() {
                    val image = loadImage(picture.thumbnail)
                    withContext(Dispatchers.Main){
                        binding.image.setImageBitmap(image)
                    }

                }
            }
        }
    }

    fun submitList(updatedPhotos: List<Photo>) {
        photos = updatedPhotos
        notifyDataSetChanged()
    }

    override fun getItemCount() = photos.size

    inner class PhotosViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

}


suspend fun loadImage(url: String): Bitmap = withContext(Dispatchers.IO) {
    val urlImage: URL = URL(url)
    urlImage.toBitmap()!!
}

private fun URL.toBitmap(): Bitmap? {
    return try {
        BitmapFactory.decodeStream(openStream())
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
