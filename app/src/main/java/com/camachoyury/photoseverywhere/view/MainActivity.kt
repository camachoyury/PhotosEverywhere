package com.camachoyury.photoseverywhere.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.camachoyury.photoseverywhere.view.ui.theme.PhotosTheme
import com.camachoyury.photoseverywhere.viewmodel.PhotosViewModel
import com.camachoyury.photoseverywhere.viewmodel.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import com.camachoyury.photoseverywhere.R


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: PhotosViewModel by viewModels()

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhotosTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    Greeting("YURY")
//                }
                Scaffold (
                    topBar = {ToolBar()},
                    content = {
                        Greeting("YURY")
                    }

                        )
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
        ToolBar()

    }
}


@Composable
fun ToolBar() {


    TopAppBar(
        modifier = Modifier.padding(3.dp),
        navigationIcon = {
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                painter = painterResource(id = R.drawable.ic_baseline_message_24),
                contentDescription = null // decorative element
            )
        },
        actions = {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jetpack_logo),
                    contentDescription = ""
                )
            }
        },
        title = { Text(text = "Google Photos") },
        elevation = 16.dp

    )
}


