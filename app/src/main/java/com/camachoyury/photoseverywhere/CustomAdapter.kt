package com.camachoyury.photoseverywhere

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camachoyury.photoseverywhere.data.entities.Photo
import com.camachoyury.photoseverywhere.databinding.ItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.util.*


class CustomAdapter(
    var photos :List<Photo>
) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding =  ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // set the view's size, margins, paddings and layout parameters
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
                    // open another activity on item click
//            val intent = Intent(context, SecondActivity::class.java)
//            intent.putExtra("image", personImages[position]) // put image data in Intent
//            context.startActivity(intent) // start Intent
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

    // url of image
    val urlImage: URL = URL(url)
    val result: Bitmap? = withContext(Dispatchers.IO){
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
