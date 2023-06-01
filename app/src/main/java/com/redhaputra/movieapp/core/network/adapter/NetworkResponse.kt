package com.redhaputra.movieapp.core.network.adapter

/**
 * Generic network response
 * @param T success body response
 */
sealed class NetworkResponse<out T : Any> {

    /**
     * Handle resource success
     */
    class Success<out T : Any>(val data: T?) : NetworkResponse<T>()

    /**
     * Handle resource error
     */
    class Error(val message: String) : NetworkResponse<Nothing>()

    override fun toString(): String =
        when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error $message"
        }
}