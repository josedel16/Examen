package com.gmail.jmdm1601.examen.core

import com.gmail.jmdm1601.examen.data.model.Movie
import com.gmail.jmdm1601.examen.data.model.MovieEntity

fun List<MovieEntity>.toMovieList(): List<Movie> {
    val resultList = mutableListOf<Movie>()
    this.forEach { movieEntity ->
        resultList.add(movieEntity.toMovie())
    }
    return resultList
}

fun MovieEntity.toMovie(): Movie = Movie(
    this.adult,
    this.backdropPath,
    this.id,
    this.originalTitle,
    this.originalLanguage,
    this.overview,
    this.popularity,
    this.posterPath,
    this.releaseDate,
    this.title,
    this.video,
    this.voteAverage,
    this.voteCount
)

fun Movie.toMovieEntity(): MovieEntity = MovieEntity(
    this.adult,
    this.backdropPath,
    this.id,
    this.originalTitle,
    this.originalLanguage,
    this.overview,
    this.popularity,
    this.posterPath,
    this.releaseDate,
    this.title,
    this.video,
    this.voteAverage,
    this.voteCount
)