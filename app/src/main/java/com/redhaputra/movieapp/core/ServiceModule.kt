package com.redhaputra.movieapp.core

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Object module that provide all the service needed to handle API Fetch.
 *
 * @see Module
 */
@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object ServiceModule {
}