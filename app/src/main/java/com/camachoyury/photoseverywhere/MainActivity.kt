package com.camachoyury.photoseverywhere

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camachoyury.photoseverywhere.data.entities.Photo
import com.camachoyury.photoseverywhere.databinding.ActivityMainBinding
import com.camachoyury.photoseverywhere.viewmodel.PhotosViewModel
import com.camachoyury.photoseverywhere.viewmodel.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: PhotosViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PhotosAdapter

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        gridLayoutManager.orientation = LinearLayoutManager.HORIZONTAL;
        binding.photosList.layoutManager = gridLayoutManager
        adapter = PhotosAdapter(emptyList())
        binding.photosList.adapter = adapter

        viewModel.load()

        lifecycleScope.launchWhenStarted {
            viewModel.photos.collect {
                when (it) {
                    is ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    }
                    is ScreenState.Success -> {
                        binding.progressBar.visibility = View.GONE
                       updatePhotos(it.photos)
                    }
                }
            }
        }
    }

    private fun updatePhotos(photos:List<Photo> ){
        adapter.submitList(photos)
//        binding.photosList.adapter = adapter
        Log.d("Photo", photos[0].toString())

    }
}