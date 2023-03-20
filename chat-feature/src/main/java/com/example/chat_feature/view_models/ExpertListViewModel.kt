package com.example.chat_feature.view_models

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_feature.data.bot_history.ChatJson
import com.example.chat_feature.data.experts.Expert
import com.example.chat_feature.data.experts.ExpertListRequest
import com.example.chat_feature.interfaces.HomeActivityCaller
import com.example.chat_feature.interfaces.HomeActivityCallerClass
import com.example.chat_feature.network.Api
import com.example.chat_feature.utils.Constants
import com.example.chat_feature.utils.Resource
import com.example.chat_feature.utils.SafeApiCall
import com.example.chat_feature.utils.getUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExpertListViewModel @Inject constructor(
    private val api: Api,
    application: Application,
) : AndroidViewModel(application), SafeApiCall {

    companion object {
        private const val TAG = "ExpertListViewModel"
    }
    //    val expertList = mutableStateListOf<Resource<List<Expert>>>()
    val experts = mutableStateOf<Resource<List<Expert>>>(Resource.Loading)


//    private val _experts = MutableStateFlow<Resource<List<Expert>>>(Resource.Loading)
//    val experts = _experts

    val userId:String
    init {
        userId=application.applicationContext.getUserId().toString()
    }
    /*init {
        viewModelScope.launch(Dispatchers.IO) {
            listenUpdates()
            loadExpertList(ExpertListRequest(senderId = sharedPref.getString("id")))
        }
    }*/

    //TODO use shredpref for apassing sender id
    fun loadExpertList(data: ExpertListRequest) = viewModelScope.launch {
        val response = safeApiCall {
            api.loadExpertList(
                senderId = data.senderId
            ).expertsList
        }
//        _experts.emit(response)
        experts.value = response
    }

    fun loadBotHistory(data: ChatJson) = viewModelScope.launch {
        val response = safeApiCall {
            api.loadChatBetweenUserAndExpert(
                senderId = data.sentBy!!,
                queryId = data.query_id!!


            )
        }
    }




}


