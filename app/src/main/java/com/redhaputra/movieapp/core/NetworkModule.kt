package com.redhaputra.movieapp.core

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Object module that provide all the network config needed.
 *
 * @see Module
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

}