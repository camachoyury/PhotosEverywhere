package com.camachoyury.photoseverywhere.di


import com.camachoyury.photoseverywhere.data.api.PhotosService
import com.camachoyury.photoseverywhere.data.repository.PhotosRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String = "https://randomuser.me/api/"

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, url: String ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providePhotosRepository(
            photosService: PhotosService
    ): PhotosRepositoryImpl {
        return PhotosRepositoryImpl(photosService)
    }

    @Provides
    @Singleton
    fun providePhotoService(retrofit: Retrofit): PhotosService =  retrofit.create(PhotosService::class.java)


}
