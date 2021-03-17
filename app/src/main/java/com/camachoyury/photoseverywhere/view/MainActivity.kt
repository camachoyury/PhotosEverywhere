package com.camachoyury.photoseverywhere.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.camachoyury.photoseverywhere.view.ui.theme.PhotosTheme
import com.camachoyury.photoseverywhere.viewmodel.PhotosViewModel
import com.camachoyury.photoseverywhere.viewmodel.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import com.camachoyury.photoseverywhere.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: PhotosViewModel by viewModels()

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhotosTheme {
                Scaffold(
                    topBar = { ToolBar() },
                    content = {
                        Drag(imageId = R.drawable.dog1)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PhotosTheme {
        Drag(imageId = R.drawable.dog1)
    }
}

@Composable
fun ToolBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                painter = painterResource(id = R.drawable.ic_baseline_message_24),
                contentDescription = null
            )
        },
        actions = {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jetpack_logo),
                    contentDescription = ""
                )
            }
        },
        title = {
            Text(
                text = "Google Photos"
            )
                },
        elevation = 16.dp
    )
}

@Composable
fun Drag(@DrawableRes imageId: Int) {

    Box(modifier = Modifier.background(Color.White).padding(8.dp).fillMaxSize()) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        val scale by remember { mutableStateOf(1f) }
        var rotation by remember { mutableStateOf(0f) }
        var offset by remember { mutableStateOf(  Offset.Zero) }

        Card(
            Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.White)
                .size(200.dp, 300.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .pointerInput(Unit) {
                    detectDragGestures(onDragEnd = {
                        rotation =  0f
                        offsetX = scale
                        offsetY = scale
                    }) { change, dragAmount ->
                        change.consumeAllChanges()
                        rotation = if (offsetX > 0){
                            -20f
                        }else 20f
                        offsetX = offsetX.plus(dragAmount.x)
                        offsetY = offsetY.plus(dragAmount.y)
//

                    }
                },
            backgroundColor= Color.White,
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Image(
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = imageId),
                contentDescription = ""
            )

            Row(
                Modifier.wrapContentHeight(align = Alignment.Bottom).background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.Black
                        )
                    )),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(Icons.Rounded.Close,
                    tint = Color.Red)
                Button(Icons.Rounded.StarRate,
                    tint = Color.Blue)
                Button( Icons.Rounded.Favorite,
                    tint = Color.Green)

            }
        }
    }
}

@Composable
fun Button(icon: ImageVector, tint: Color ){

    Surface(
        modifier = Modifier.padding(12.dp).size(54.dp),
        border = BorderStroke(2.dp, tint),
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.1f)
    ) {
        Icon(
            icon,
            modifier=Modifier.padding(4.dp).size(24.dp),
            tint = tint,
            contentDescription = "Localized description")
    }
}





