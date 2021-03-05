package com.camachoyury.photoseverywhere

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camachoyury.photoseverywhere.databinding.ActivityMainBinding
import com.camachoyury.photoseverywhere.viewmodel.PhotosViewModel
import com.camachoyury.photoseverywhere.viewmodel.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.io.IOException
import java.net.URL


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: PhotosViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        gridLayoutManager.orientation = LinearLayoutManager.HORIZONTAL; // set Horizontal Orientation

        binding.photosList.layoutManager = gridLayoutManager

        viewModel.load()
        lifecycleScope.launchWhenStarted {
            viewModel.photos.collect {
                when (it) {
                    is ScreenState.Loading -> {
                    }
                    is ScreenState.Success -> {
                        var adapter = CustomAdapter(it.photos)
                        binding.photosList.adapter = adapter
                        Log.d("Photo", it.photos[0].toString())
                    }
                }

            }
        }
    }


}