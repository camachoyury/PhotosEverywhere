package com.camachoyury.photoseverywhere.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.camachoyury.photoseverywhere.R
import kotlinx.coroutines.*
import okhttp3.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ImageStick(context: Context) {

    var memoryCache = MemoryCache()
    private var fileCache: FileCache = FileCache(context)
    private val imageViews = Collections.synchronizedMap(WeakHashMap<ImageView, String>())
    val client = OkHttpClient()  // preinitialize the client


    fun showImage(url: String, imageView: ImageView) {

        imageViews[imageView] = url
        val bitmap = memoryCache[url]

        if (bitmap != null) imageView.setImageBitmap(bitmap) else {

            GlobalScope.launch(Dispatchers.Main) {
                loadImage(PhotoToLoad(url, imageView))
                imageView.setImageResource(R.drawable.jetpack_logo)

            }
        }
    }

    private suspend fun loadImage(photoToLoad: PhotoToLoad) {


            if (!imageViewReused(photoToLoad)) {

                val bmp = createBitmapFromUrl(photoToLoad.url)
                if (bmp != null) {
                    memoryCache.put(photoToLoad.url, bmp)
                    photoToLoad.imageView.setImageBitmap(bmp)
                } else
                    photoToLoad.imageView.setImageResource(
                        R.drawable.ic_launcher_background
                    )
            }


    }

    private suspend fun getBitmap(url: String): Bitmap? {


        val f: File = fileCache.getFile(url)
//
//        //from SD cache
//        val b = decodeFile(f)
//        return b ?:
       return try {
            var bitmap: Bitmap? = null
            val imageUrl = URL(url)
            val conn = imageUrl.openConnection() as HttpURLConnection
            conn.connectTimeout = 30000
            conn.readTimeout = 30000
            conn.instanceFollowRedirects = true
            val inputStream = conn.inputStream
            val os: OutputStream = FileOutputStream(f)
            copyStream(inputStream, os)
            os.close()
            bitmap = decodeFile(f)
            bitmap
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }


    }

    class PhotoToLoad(var url: String, var imageView: ImageView)
    private fun imageViewReused(photoToLoad: PhotoToLoad): Boolean {
        val tag: String? = imageViews[photoToLoad.imageView]
        return tag == null || tag != photoToLoad.url
    }

    suspend fun createBitmapFromUrl(url: String): Bitmap? = withContext(Dispatchers.IO) {
        val req = Request.Builder().url(url).build()
        val res = client.newCall(req).await()

        val result: Deferred<Bitmap?> = GlobalScope.async {
            BitmapFactory.decodeStream(res.body?.byteStream())
        }

        result.await()



    }

    internal suspend inline fun Call.await(): Response {
        return suspendCancellableCoroutine { continuation ->
            val callback = ContinuationCallback(this, continuation)
            enqueue(callback)
            continuation.invokeOnCancellation(callback)
        }
    }
    private suspend fun decodeFile(f: File): Bitmap? = withContext(Dispatchers.IO) {

        try {
            //decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            withContext(Dispatchers.Default) {
                BitmapFactory.decodeStream(FileInputStream(f), null, o)
            }
            //Find the correct scale value. It should be the power of 2.
            val requiredSize = 70
            var widthTmp = o.outWidth
            var heightTmp = o.outHeight
            var scale = 1
            while (true) {
                if (widthTmp / 2 < requiredSize || heightTmp / 2 < requiredSize) break
                widthTmp /= 2
                heightTmp /= 2
                scale *= 2
            }

            //decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            withContext(Dispatchers.Default) {
                BitmapFactory.decodeStream(
                    FileInputStream(f),
                    null,
                    o2
                )
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
         null
    }


    private fun copyStream(inputStream: InputStream, os: OutputStream) {
        val bufferSize = 1024
        try {
            val bytes = ByteArray(bufferSize)
            while (true) {
                val count = inputStream.read(bytes, 0, bufferSize)
                if (count == -1) break
                os.write(bytes, 0, count)
            }
        } catch (ex: java.lang.Exception) {
        }
    }
}


// extension function to get bitmap from url
fun URL.toBitmap(): Bitmap?{
    return try {
        BitmapFactory.decodeStream(openStream())
    }catch (e:IOException){
        null
    }
}



internal class ContinuationCallback(
    private val call: Call,
    private val continuation: CancellableContinuation<Response>
) : Callback, CompletionHandler {


    override fun onResponse(call: Call, response: Response) {
        continuation.resume(response)
    }

    override fun onFailure(call: Call, e: IOException) {
        if (!call.isCanceled()) {
            continuation.resumeWithException(e)
        }
    }

    override fun invoke(cause: Throwable?) {
        try {
            call.cancel()
        } catch (_: Throwable) {}
    }
}


