package com.gmail.jmdm1601.examen.data.remote

import com.gmail.jmdm1601.examen.core.Constants
import com.gmail.jmdm1601.examen.data.model.MoviesResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String = Constants.API_KEY): MoviesResponse

    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}