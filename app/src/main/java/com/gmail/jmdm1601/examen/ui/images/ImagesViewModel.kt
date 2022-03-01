package com.gmail.jmdm1601.examen.ui.images

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.jmdm1601.examen.core.Resource
import com.gmail.jmdm1601.examen.data.remote.FirebaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class ImagesViewModel @Inject constructor() : ViewModel() {

    private val _result = MutableLiveData<Resource<Unit>>()
    private val _images = MutableLiveData<List<String>?>()

    val result: LiveData<Resource<Unit>> = _result
    val images: LiveData<List<String>?> = _images

    init {
        viewModelScope.launch {
            FirebaseService.getImages().collect {
                _images.postValue(it)
            }
        }
    }

    fun uploadImage(file: Uri) {
        _result.postValue(Resource.Loading)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _result.postValue(FirebaseService.uploadFile(file))
            }
        }
    }

    fun uploadBitmap(bitmap: Bitmap) {
        _result.postValue(Resource.Loading)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
                val data: ByteArray = baos.toByteArray()

                _result.postValue(FirebaseService.uploadBytes(data))
            }
        }
    }
}