package com.redhaputra.movieapp.core

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Object module that provide all the repository needed to handle API implementation.
 *
 * @see Module
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
}