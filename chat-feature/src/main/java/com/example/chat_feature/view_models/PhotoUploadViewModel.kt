package com.example.chat_feature.view_models


import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_feature.data.response.UploadPhotoResponse
import com.example.chat_feature.network.Api
import com.example.chat_feature.utils.Resource
import com.example.chat_feature.utils.SafeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PhotoUploadViewModel @Inject constructor(
    private val api: Api
) : ViewModel(), SafeApiCall
{


    private val _response = MutableLiveData<Resource<UploadPhotoResponse>>()
    val response: LiveData<Resource<UploadPhotoResponse>> = _response

    fun uploadImage(image: File) {
        viewModelScope.launch(Dispatchers.IO) {

            //val requestFile = image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val requestFile = image.asRequestBody("*/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", image.name, requestFile)

            var response = safeApiCall { api.uploadPhoto(body) }
            Log.d("fileName", body.toString())
            when (response) {
                Resource.Loading -> _response.postValue(Resource.Loading)
                is Resource.Failure -> _response.postValue(response)
                is Resource.Success -> _response.postValue(response)

            }

        }
    }
}