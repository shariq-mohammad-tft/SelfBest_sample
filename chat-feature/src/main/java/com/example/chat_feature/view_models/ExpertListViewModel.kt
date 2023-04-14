package com.example.chat_feature.view_models

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_feature.data.bot_history.ChatJson
import com.example.chat_feature.data.experts.Expert
import com.example.chat_feature.data.experts.ExpertListRequest
import com.example.chat_feature.network.Api
import com.example.chat_feature.utils.Resource
import com.example.chat_feature.utils.SafeApiCall
import com.example.chat_feature.utils.getUserId
import com.example.chat_feature.utils.normalText
import dagger.hilt.android.lifecycle.HiltViewModel
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

    var stateSearch by mutableStateOf(ActorsScreenState())

    fun onAction(userAction: UserAction){
        when(userAction){
            UserAction.CloseIconClicked -> {
                stateSearch=stateSearch.copy(
                    isSearchBarVisible = false
                )
            }
            UserAction.SearchIconClicked -> {
                stateSearch=stateSearch.copy(
                    isSearchBarVisible = true
                )
            }

            is UserAction.TextFieldInput -> {
                stateSearch=stateSearch.copy(
                    searchText = userAction.text
                )
                searchActorsInList(userAction.text)
            }

        }
    }


    private fun searchActorsInList(searchQuery:String){
        when (val expertsResource = experts.value) {
            is Resource.Success -> {
                val newList = if (searchQuery.isNullOrEmpty()) {
                    expertsResource.value
                } else {
                    expertsResource.value.filter {
                        it.fullName.contains(searchQuery, ignoreCase = true)
                    }
                }
                stateSearch = stateSearch.copy(list = newList)
                Log.d("filtered", newList.toString())
            }
            else -> {
                // Handle loading and error states as necessary
            }
        }
    }


    //TODO use shredpref for apassing sender id
    fun loadExpertList(data: ExpertListRequest) = viewModelScope.launch {
        val response = safeApiCall {
            api.loadExpertList(
                senderId = data.senderId
                //senderId = "736"
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

    sealed class UserAction {
        object SearchIconClicked : UserAction()
        object CloseIconClicked : UserAction()

        data class TextFieldInput(val text: String) : UserAction()
    }

    data class ActorsScreenState(
        val searchText: String = "",
        val isSearchBarVisible: Boolean = false,
        val isSortMenuVisible: Boolean = false,
        val list: List<Expert> = emptyList()
    )




}


