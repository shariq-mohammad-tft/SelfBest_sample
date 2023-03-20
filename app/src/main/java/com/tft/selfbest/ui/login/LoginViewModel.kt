package com.tft.selfbest.ui.login


import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.GoogleLoginRequest
import com.tft.selfbest.models.GoogleLoginRequest2
import com.tft.selfbest.models.LogedInData
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.network.NetworkResponse.Loading
import com.tft.selfbest.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pref: SelfBestPreference,
    private val repository: LoginRepository
) : ViewModel() {

    private val loginLiveData = MutableLiveData<NetworkResponse<LogedInData>>()
    val loginApiObserver: LiveData<NetworkResponse<LogedInData>> = loginLiveData

    /* fun login(loginRequest: LoginRequest) {
         if (loginLiveData.value is Loading) {
             return
         }
         viewModelScope.launch {
             repository.login(loginRequest).collect {
                 loginLiveData.postValue(it)
             }
         }
     }*/

    fun googleLogin(data: GoogleSignInAccount, userType: String) {
        if (loginLiveData.value is Loading || data.idToken == null) {
            return
        }
        viewModelScope.launch {
            Log.e("Google ", "${data.idToken}")
            repository.loginViaGoogle(
                GoogleLoginRequest(
                    userType, data.idToken!!
                )
            ).collect {
                loginLiveData.postValue(it)
            }
        }
    }

    fun googleLogin(data: GoogleSignInAccount, userType: String, reactivate: Boolean) {
        if (loginLiveData.value is Loading || data.idToken == null) {
            return
        }
        viewModelScope.launch {
            Log.e("Google ", "${data.idToken}")
            repository.loginViaGoogle(
                GoogleLoginRequest2(
                    userType, data.idToken!!, reactivate
                )
            ).collect {
                loginLiveData.postValue(it)
            }
        }
    }

    fun linkedInLogin(code: String, type: String,  reactivate: Boolean) {
        if (loginLiveData.value is Loading) {
            return
        }
        viewModelScope.launch {
            Log.e("LinkedIn", "$code $type")
            repository.loginViaLinkedIn(code, "Asia/Calcutta", type, reactivate).collect {
                loginLiveData.postValue(it)
            }
        }
    }

    fun savedLoginData(data: LogedInData) {
        pref.setLoginData(data)
    }
}

