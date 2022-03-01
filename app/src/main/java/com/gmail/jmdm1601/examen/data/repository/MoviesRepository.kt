package com.gmail.jmdm1601.examen.data.repository

import com.gmail.jmdm1601.examen.core.toMovie
import com.gmail.jmdm1601.examen.core.toMovieEntity
import com.gmail.jmdm1601.examen.core.toMovieList
import com.gmail.jmdm1601.examen.data.local.MoviesDao
import com.gmail.jmdm1601.examen.data.model.Movie
import com.gmail.jmdm1601.examen.data.remote.ApiService
import java.lang.Exception
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val service: ApiService,
    private val moviesDao: MoviesDao
) {

    suspend fun getPopularMovies(): List<Movie> {
        try {
            val result = service.getPopularMovies()
            moviesDao.deleteAll()
            result.movies.forEach {
                moviesDao.insert(it.toMovieEntity())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return moviesDao.getAll().toMovieList()
    }

    suspend fun getMovie(id: Long): Movie {
        return moviesDao.getById(id).toMovie()
    }
}