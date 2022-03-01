package com.gmail.jmdm1601.examen.core

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure(val exception: Exception) : Resource<Nothing>()
}