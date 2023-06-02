package com.redhaputra.movieapp.core.di.module

import com.redhaputra.movieapp.core.network.repositories.MovieRepository
import com.redhaputra.movieapp.core.network.services.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Object module that provide all the repository needed to handle API implementation.
 *
 * @see Module
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Create a provider method binding for [MovieRepository].
     *
     * @return Instance of Movie Repository.
     * @see Provides
     */
    @Singleton
    @Provides
    fun provideMovieRepository(service: MovieService) = MovieRepository(service)
}