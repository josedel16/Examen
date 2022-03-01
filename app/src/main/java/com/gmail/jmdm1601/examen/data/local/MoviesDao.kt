package com.gmail.jmdm1601.examen.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gmail.jmdm1601.examen.data.model.MovieEntity

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movies")
    suspend fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE id=:id")
    suspend fun getById(id: Long): MovieEntity

    @Query("DELETE FROM movies")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(movie: MovieEntity)
}