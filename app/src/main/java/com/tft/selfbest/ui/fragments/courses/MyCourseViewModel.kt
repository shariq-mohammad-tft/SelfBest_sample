package com.tft.selfbest.ui.fragments.courses

import androidx.lifecycle.*
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.mycourse.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.repository.MyCourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyCourseViewModel @Inject constructor(
    private val preferences: SelfBestPreference,
    private val myCourseRepository: MyCourseRepository
) : ViewModel(), LifecycleObserver {
    private val enrolledCourseMutableLiveData =
        MutableLiveData<NetworkResponse<EnrolledCourseResponse>>()
    val enrolledCourseObserver: LiveData<NetworkResponse<EnrolledCourseResponse>> =
        enrolledCourseMutableLiveData
    private val suggestedCourseMutableLiveData =
        MutableLiveData<NetworkResponse<SuggestedCourseResponse>>()
    val suggestedCourseObserver: LiveData<NetworkResponse<SuggestedCourseResponse>> =
        suggestedCourseMutableLiveData
    private val searchCourseMutableLiveData =
        MutableLiveData<NetworkResponse<List<SuggestedCourse>>>()
    val searchResultCourseObserver: LiveData<NetworkResponse<List<SuggestedCourse>>> =
        searchCourseMutableLiveData
    private val addCourseMutableLiveData = MutableLiveData<String>()
    val addCourseObserver: LiveData<String> = addCourseMutableLiveData

    fun getEnrolledCourses() {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            myCourseRepository.getEnrolledCourses(userId).collect {
                if (it is NetworkResponse.Success) {
                    enrolledCourseMutableLiveData.postValue(it)
                }
            }
        }
    }

    fun getSuggestedCourses() {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            myCourseRepository.getSuggestedCourses(userId, "GO", 1).collect {
                if (it is NetworkResponse.Success)
                    suggestedCourseMutableLiveData.postValue(it)
            }
        }
    }

    fun getSearchCourseResult(filterSearchCourse: FilterSearchCourse) {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            myCourseRepository.getSearchResult(userId, filterSearchCourse).collect {
                if (it is NetworkResponse.Success)
                    searchCourseMutableLiveData.postValue(it)
            }
        }
    }

    fun addCourse(addCourse: AddCourse) {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            myCourseRepository.addCourse(userId, addCourse).collect {
                if (it is NetworkResponse.Success) {
                    addCourseMutableLiveData.value =
                        "${addCourse.courseName} added successfully"
                    getEnrolledCourses()
                }

            }
        }
    }

    fun uploadCertificate(fileUrl: File?, courseId: String) {
        viewModelScope.launch {
            val userId = preferences.getLoginData?.id ?: return@launch
            myCourseRepository.uploadCertificate(userId, courseId, fileUrl).collect {
                if (it is NetworkResponse.Success)
                    addCourseMutableLiveData.value =
                        "Certificated uploaded successfully"
            }
        }
    }
}