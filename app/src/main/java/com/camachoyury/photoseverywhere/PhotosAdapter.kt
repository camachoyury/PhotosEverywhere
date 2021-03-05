package com.camachoyury.photoseverywhere

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.camachoyury.photoseverywhere.data.entities.Photo
import com.camachoyury.photoseverywhere.databinding.ItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL


class CustomAdapter(
    var photos :List<Photo>
) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =  ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(photos[position]){
                binding.name.text = name.first
                GlobalScope.launch {
                    loadImage(binding.image, picture.thumbnail)
                }
                binding.root.setOnClickListener {

                }
            }

        }

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener {
            // open another activity on item click
//            val intent = Intent(context, SecondActivity::class.java)
//            intent.putExtra("image", personImages[position]) // put image data in Intent
//            context.startActivity(intent) // start Intent
        }
    }

    override fun getItemCount() = photos.size

    inner class MyViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {

        }

}
suspend fun loadImage(imageView: ImageView, url: String){

    val urlImage: URL = URL(url)
    val result: Bitmap? = withContext(Dispatchers.IO){
        println("   'runBlocking': I'm working in thread ${Thread.currentThread().name}")
        urlImage.toBitmap()
    }
    GlobalScope.launch(Dispatchers.Main){
        imageView.setImageBitmap(result)
    }
}

// extension function to get bitmap from url
private fun URL.toBitmap(): Bitmap?{
    return try {
        BitmapFactory.decodeStream(openStream())
    }catch (e: IOException){
        null
    }
}
