package com.redhaputra.movieapp.core.di.module

import com.redhaputra.movieapp.core.network.services.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Object module that provide all the service needed to handle API Fetch.
 *
 * @see Module
 */
@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object ServiceModule {
    /**
     * Create a provider method binding for [MovieService].
     *
     * @return Instance of Movie service.
     * @see Provides
     */
    @Singleton
    @Provides
    fun provideMovieService(
        @Named("authClient") client: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): MovieService = retrofitBuilder
        .client(client)
        .build()
        .create(MovieService::class.java)
}