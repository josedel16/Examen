package com.gmail.jmdm1601.examen.ui.movies

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
class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModel() {

    private val _movies = MutableLiveData<Resource<List<Movie>>>()

    val movies: LiveData<Resource<List<Movie>>> = _movies

    init {
        viewModelScope.launch {
            _movies.postValue(Resource.Loading)
            withContext(Dispatchers.IO) {
                _movies.postValue(Resource.Success(moviesRepository.getPopularMovies()))
            }
        }
    }
}