package com.camachoyury.photoseverywhere

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.camachoyury.photoseverywhere.databinding.ActivityMainBinding
import com.camachoyury.photoseverywhere.util.ImageStick
import com.camachoyury.photoseverywhere.viewmodel.PhotosViewModel
import com.camachoyury.photoseverywhere.viewmodel.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: PhotosViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)


        val imageStick = ImageStick(applicationContext)

        imageStick.otherImage( "https://randomuser.me/api/portraits/med/men/31.jpg" ,binding.image)



//        viewModel.load()
//        lifecycleScope.launchWhenStarted {
//            viewModel.photos.collect {
//                when (it) {
//                    is ScreenState.Loading -> {
//
//                    }
//                    is ScreenState.Success -> {
//                        Log.d("Photo", it.photos[0].toString())
//                    }
//                }
//
//            }
//        }
    }
}