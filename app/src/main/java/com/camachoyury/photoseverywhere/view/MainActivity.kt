package com.camachoyury.photoseverywhere.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.camachoyury.photoseverywhere.view.ui.theme.PhotosTheme
import com.camachoyury.photoseverywhere.viewmodel.PhotosViewModel
import com.camachoyury.photoseverywhere.viewmodel.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: PhotosViewModel by viewModels()

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            PhotosTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("YURY")
                }
            }
        }


        viewModel.load()
        lifecycleScope.launchWhenStarted {
            viewModel.photos.collect {
                when (it) {
                    is ScreenState.Loading -> {

                    }
                    is ScreenState.Success -> {

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    Text(text = "Hello $name!")
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PhotosTheme {
       Greeting(name = "YURY CAMACHO")

    }
}