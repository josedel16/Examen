package com.gmail.jmdm1601.examen.ui.movies.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.jmdm1601.examen.core.Resource
import com.gmail.jmdm1601.examen.data.model.Movie
import com.gmail.jmdm1601.examen.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModel() {

    private val _movie = MutableLiveData<Resource<Movie>>()

    val movie: LiveData<Resource<Movie>> = _movie

    fun getMovie(id: Long) {
        viewModelScope.launch {
            _movie.postValue(Resource.Loading)
            withContext(Dispatchers.IO) {
                _movie.postValue(Resource.Success(moviesRepository.getMovie(id)))
            }
        }
    }
}