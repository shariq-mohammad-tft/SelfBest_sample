package com.tft.selfbest.network

import com.tft.selfbest.data.entity.SelfBestNotification
import com.tft.selfbest.models.*
import com.tft.selfbest.models.calendarEvents.EventDetail
import com.tft.selfbest.models.mycourse.*
import com.tft.selfbest.models.notifications.NotificationResponse
import com.tft.selfbest.models.overview.*
import com.tft.selfbest.models.suggestedApps.AppDetail
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

interface SelfBestApi {

    @GET("user/getnotification")
    suspend fun getNotificationInfo(
        @Query("Email") email: String,
        @Query("Type") type: String,
        @Query("loc") rimeZone: String,
    ): SelfBestNotification

    @GET("user/getobservation")     //replace this with set observation
    suspend fun getObservation(
        @Query("Email") email: String,
        @Query("Url") type: String?,
    ): DistractedObservationResponse

    @GET("user/{user_id}/dashboard/distraction")
    suspend fun getDistractedApps(@Path("user_id") userID: Int): DistractionAppResponse

    @GET("user/{user_id}/dashboard/profile")
    suspend fun getProfile(@Path("user_id") userID: Int,): ProfileResponse

    @GET("user/{user_id}/dashboard/profile")
    suspend fun getProfile(
        @Path("user_id") userID: Int,
        @Query("platform") platform: String
    ): ProfileResponse

    @GET("sheet/job-positions")
    suspend fun getJobs(): List<String>

    @GET("sheet/onetcenter/skills")
    suspend fun getAllSkills(
        @Query("q") q: String,
        @Query("organisation_id") organisation_id: String
    ): List<String>

    @POST("login?loc=Asia/Kolkata")
    suspend fun login(@Body loginRequest: LoginRequest): LogedInData

    @POST("user/{user_id}/setdeviceobservation")
    suspend fun sendDistractedAppUsage(
        @Path("user_id") userID: Int,
        @Body data: SendUsedDistractedApps,
    )

    @POST("user/{user_id}/getgoday/getgohourdetails")
    suspend fun getGoHourDetails(
        @Path("user_id") userID: Int,
        @Query("loc") timeZone: String,
    ): GoHourResponse

    @POST("user/{user_id}/device-observation")
    suspend fun sendDeviceObservation(
        @Path("user_id") userID: Int,
        @Body data: DeviceObservationRequest,
    ): DeviceObservationResponse

    @POST("user/{user_id}/device-observations")
    suspend fun sendOfflineDeviceObservations(
        @Path("user_id") userID: Int,
        @Body deviceObservationsData: SendOfflineDeviceObservations,
    ): DeviceObservationResponse

    @GET("user/{user_id}/getgoday/status")
    suspend fun getGoHourStatus(
        @Path("user_id") userID: Int,
        @Query("location") location: String,
    ): GoHourResponse

    @GET("user/{user_id}/getgoday/status")
    suspend fun getGoHourStatusWithStartTime(
        @Path("user_id") userID: Int,
        @Query("location") location: String,
        @Query("startTime") startTime: String,
    ): GoHourStatusResponse

    @POST("google/auth?location=Asia/Calcutta")
    suspend fun loginViaGoogle(@Body googleLoginRequest: GoogleLoginRequest): LogedInData

    @POST("google/auth?location=Asia/Calcutta")
    suspend fun loginViaGoogle(@Body googleLoginRequest: GoogleLoginRequest2): LogedInData

    @GET("linkedin/auth")
    suspend fun loginViaLinked(
        @Query("code") code: String,
        @Query("location") location: String,
        @Query("type") type: String,
        @Query("reactivate") reactivate: Boolean
    ): LogedInData

    @GET("/user/{user_id}/getgoday/activity")
    suspend fun overViewActivity(
        @Path("user_id") userID: Int,
        @Query("loc") location: String,
    ): OverViewActivityResponse

    @GET("/user/{user_id}/getgoday/level")
    suspend fun overViewLevel(
        @Path("user_id")
        userID: Int,
    ): OverViewLevelResponse

    @GET("/user/{user_id}/dashboard/overviewtop")
    suspend fun getOverViewDistractedTime(
        @Path("user_id") userID: Int,
        @Query("location") location: String,
    ): OverViewDistractedResponse

    @GET("/user/{user_id}/getgoday/stats")
    suspend fun getOverviewCompletedTime(@Path("user_id") userID: Int): OverViewCompletedResponse

    @GET("/user/{user_id}/getgoday/suggested-app")
    suspend fun getSuggestedApps(@Path("user_id") userID: Int): List<AppDetail>

    @POST("/user/{user_id}/getgoday/suggested-app")
    suspend fun addInSuggestedAppList(
        @Path("user_id") userID: Int,
        @Body appDetail: AppDetail,
    ): Int

    @GET("user/{user_id}/courses")
    suspend fun getCourses(@Path("user_id") userID: Int): OverviewCourses

    @GET("/user/{user_id}/calendar/events")
    suspend fun getUpcomingEvents(
        @Path("user_id") userID: Int,
        @Query("location") location: String,
        @Query("day") day: Int,
        @Query("month") month: String,
        @Query("year") year: Int,
    ): List<EventDetail>

//    @GET("/user/{user_id}/calendar/events")
//    suspend fun getUpcomingEvents(
//        @Path("user_id") userID: Int,
//        @Query("location") location: String,
//        @Query("day") day: Int,
//        @Query("month") month: String,
//        @Query("year") year: Int,
//    ): List<EventDetail>

    @POST("/user/{user_id}/calendar/event")
    suspend fun addOneDayEvent(
        @Path("user_id") userID: Int,
        @Body eventDetail: EventDetail,
    )

    @GET("/user/{user_id}/dashboard/getnotification")
    suspend fun getNotification(@Path("user_id") userID: Int): NotificationResponse

    @GET("/sheet/personalitymap")
    suspend fun getPersonality(): List<String>

    @Multipart
    @PATCH("/user/profile-photo")
    suspend fun updateProfilePicture(
        @Part image: MultipartBody.Part,
        @Part("userID") userID: RequestBody,
    ): ProfileImageResponse

    @GET("/user/{user_id}/calendar/disconnect")
    suspend fun disConnectCalendar(@Path("user_id") userID: Int)

    @GET("/user/{user_id}/calendar/connect")
    suspend fun connectCalendar(
        @Path("user_id") userID: Int,
        @Query("code") code: String,
        @Query("location") location: String,
        @Query("redirect_url") redirectUrl: String,
        @Query("calendar_type") type: String,
    )

    @GET("/user/{user_id}/managers")
    suspend fun getManagers(@Path("user_id") userID: Int): List<ManagerEmails>

    @GET("/user/{user_id}/getgoday/stats")
    suspend fun observations(
        @Path("user_id") userID: Int,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
    ): ObservationsResponse

    @GET("/sheet/onetcenter/skills")
    suspend fun getSkills(): List<String>

    @PUT("/user/{user_id}/detail")
    suspend fun saveProfileChanges(
        @Path("user_id") userID: Int,
        @Body profileChangesData: ProfileChangesData
    )

    @POST("/user/{user_id}/getgoday/timesheet-achievement-mail")
    suspend fun sendTimeSheetToManager(
        @Path("user_id") userID: Int,
        @Body profileChangesData: SelectedTimeSheetData
    )

    @GET("/user/{user_id}/courses")
    suspend fun getEnrolledCourses(
        @Path("user_id") userID: Int
    ): EnrolledCourseResponse

    @GET("/user/{user_id}/course/suggested?skill=GO&platform=0")
    suspend fun getSuggestedCourses(
        @Path("user_id") userID: Int,
        @Query("skill") skill: String,
        @Query("platform") platform: Int
    ): SuggestedCourseResponse

    @POST("/user/{user_id}/course/search")
    suspend fun getSearchResult(
        @Path("user_id") userID: Int,
        @Body filterSearchCourse: FilterSearchCourse
    ): List<SuggestedCourse>

    @POST("/user/{user_id}/course")
    suspend fun addCourse(
        @Path("user_id") userID: Int, @Body addCourse: AddCourse
    )

    @Multipart
    @POST("/user/{user_id}/course/upload-certificate")
    suspend fun uploadCertificate(
        @Path("user_id") userID: Int,
        @Part certificate: MultipartBody.Part,
        @Part("CourseId") courseId: RequestBody
    )

    //Activity Log screen
    @GET("/user/{user_id}/category/activity-logs")
    suspend fun getActivityLogs(
        @Path("user_id") userID: Int,
        @Query("type") type: String,
        @Query("event") event: String
    ) : ActivityLogValuesHigh

    //for custom dates activity log
    @GET("/user/{user_id}/category/activity-logs")
    suspend fun getActivityLogs(
        @Path("user_id") userID: Int,
        @Query("type") type: String,
        @Query("event") event : String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String
    ) : ActivityLogValuesHigh

    //Distraction
    @POST("/user/{user_id}/dashboard/distraction")
    suspend fun addDistraction(
        @Path("user_id") userID: Int,
        @Body addDistraction : AddDistraction
    )

    @DELETE("/user/{user_id}/dashboard/distraction/{id}")
    suspend fun deleteDistraction(
        @Path("user_id") userID: Int,
        @Path("id") id: Int
    )

    @PATCH("/user/{user_id}/dashboard/toggledistraction")
    suspend fun toggleDistraction(
        @Path("user_id") userID: Int,
        @Body togDist : ToggleDistraction
    )

    @GET("/user/{user_id}/getgohour")
    suspend fun getGoHour(
        @Path("user_id") userID: Int
    ) : GetGoHourResponse

    @POST("/user/{user_id}/getgohour/get-started?loc=Asia/Calcutta")
    suspend fun getStarted(
        @Path("user_id") userID: Int,
        @Body startTime : StartTime
    )

    @POST("/user/{user_id}/getgohour/start")
    suspend fun start(
        @Path("user_id") userID: Int,
        @Query("loc") loc : String,
        @Body startStatus: StartBody
    )

    @POST("/user/{user_id}/getgohour/pause?loc=Asia/Calcutta")
    suspend fun pause(
        @Path("user_id") userID: Int,
//        @Query("loc") loc: String,
        @Body startTime: StartTime
    )

    @PATCH("/user/{user_id}/getgohour/end")
    suspend fun end(
        @Path("user_id") userID: Int,
        @Query("loc") loc : String,
        @Body endTime : EndTime
    )

    @PATCH("/user/{user_id}/getgohour/reset")
    suspend fun reset(
        @Path("user_id") userID: Int,
        @Query("loc") loc:String,
        @Body startTime : StartTime
    ): Reset

    @PATCH("/user/{user_id}/getgohour/resume?loc=Asia/Calcutta")
    suspend fun resume(
        @Path("user_id") userID: Int,
//        @Query("loc") loc: String,
        @Body EndTime: EndTime
    ): TotalPauseTime

    @PATCH("user/{user_id}/getgohour/timeinterval")
    suspend fun timeInterval(
        @Path("user_id") userID: Int,
        @Query("loc") loc:String,
        @Body timeInterval: TimeInterval
    )

    @GET("/user/{user_id}/getgohour/activities?location=Asia/Calcutta")
    suspend fun getActivity(
        @Path("user_id") userID: Int,
    ): ActivityResponse

    @GET("/user/{user_id}/getgohour/activities")
    suspend fun getChartData(
        @Path("user_id")userID :Int,
        @Query("location") location:String
    ): GetChartData

    @PUT("/user/{user_id}/getgohour/complete")
    suspend fun inputProgressComplete(
        @Path("user_id") userID:Int,
        @Body inputData : InputData
    )

    @POST("/user/{user_id}/detail")
    suspend fun saveData(
        @Path("user_id") userID: Int,
        @Body signUpData: SignUpDetail
    )

//    @GET("https://googlebot.self.best/userresponses/helpsession-details")
//    suspend fun getQuery(
//        @Query("email") email: String
//    ): List<QueryResponse>

    //new url from BE for query table
    @GET("https://webbot.self.best/userresponses/helpsession-details")
    suspend fun getQuery(
        @Query("id") id: Int,
        @Query("start_date") start_date: String,
        @Query("type") type:String
    ): QueryTableResponse

    @GET("https://webbot.self.best/userresponses/helpsession-details")
    suspend fun getQuery(
        @Query("id") id: Int,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("type") type:String
    ): QueryTableResponse

    @GET("https://webbot.self.best/userresponses/helpsession-details-expert")
    suspend fun getAnsweredQuery(
        @Query("email") email: String,
        @Query("team_id") team_id: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("type") type: String
    ): List<QueryAnsweredResponse>

    @FormUrlEncoded
    @POST("https://webbot.self.best/userresponses/update-status/")
    suspend fun updateStatus(
        @Field("id") id: Int,
        @Field("status") status: Int,
        @Field("db_detail") db_detail: String
    ): UpdationBody

    @FormUrlEncoded
    @POST("https://webbot.self.best/userresponses/update-rating/")
    suspend fun updateRating(
        @Field("id") id: Int,
        @Field("rating") rating: Int,
        @Field("db_detail") db_detail: String
    ): UpdationBody

    @FormUrlEncoded
    @POST("https://webbot.self.best/userresponses/update-relevance/")
    suspend fun updateRelevance(
        @Field("id") id: Int,
        @Field("relevance") status: Int,
        @Field("db_detail") db_detail: String
    ): UpdationBody

    @GET("/user/{user_id}/skill-recommendation")
    suspend fun getRecommendation(
        @Path("user_id") userID: Int,
        @Query("q") q: String
    ): List<String>

    @Multipart
    @POST("/user/{user_id}/resume-upload")
    suspend fun uploadResume(
        @Path("user_id") userID: Int,
        @Part resume: MultipartBody.Part,
    ):UploadResumeResponse

    @GET("/user/{user_id}/getgoday/timeline")
    suspend fun getTimelineData(
        @Path("user_id") userID: Int,
        @Query("location") loc: String
    ): List<ActivityTimelineResponse>?

    @GET("/user/{user_id}/getgoday/pointsboard")
    suspend fun getPoints(
        @Path("user_id") userID: Int,
        @Query("location") loc: String,
        @Query("event") event: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<PointGraphResponse>

    //user management part
    @GET("/user/{user_id}/organisation/user-requests")
    suspend fun getUserRequests(
        @Path("user_id") userID: Int,
        @Query("status") status: String
    ): List<UserRequest>

    @GET("/user/{user_id}/organisation/pending-delete-deactivate-account-list")
    suspend fun getDeleteAccounts(
        @Path("user_id") userID: Int
    ): List<DeleteAccountResponse>

    @GET("/user/{user_id}/organisation/skills")
    suspend fun getSkillRequests(
        @Path("user_id") userID: Int
    ): SkillRequest

    @PATCH("/user/{user_id}/organisation/status")
    suspend fun changeRequestStatus(
        @Path("user_id") userID: Int,
        @Body changeRequestStatus : ChangeRequestStatus
    )

    @PATCH("/user/{user_id}/organisation/skill")
    suspend fun changeSkillRequestStatus(
        @Path("user_id") userID: Int,
        @Body changeSkillRequestStatus : ChangeSkillRequestStatus
    )

    @GET("/user/{user_id}/managerdashboard/team/certificate-status")
    suspend fun getCertificateRequest(
        @Path("user_id") userID: Int,
        @Query("team_id") team_id: Int
    )

    @POST("/user/{user_id}/pending-user-account-operations")
    suspend fun changeAccountRequest(
        @Path("user_id") userID: Int,
        @Body changeAccountRequestBody: ChangeAccountRequestBody
    )

    @DELETE("/user/{user_id}/user-account-operations")
    suspend fun accountSetting(
        @Path("user_id") userID: Int,
        @Query("type") type: String
    )

    //need to change this production API
    @POST("https://webbot.self.best/userresponses/user-device-token/")
    suspend fun sendRegistrationToken(
        @Body request: DeviceTokenRequest
    ): DeviceTokenResponse

    //for company profile
    @GET("/user/{user_id}/organisation/details")
    suspend fun getCompanyDetail(
        @Path("user_id") userID: Int
    ): CompanyProfileDetail

    @GET("/user/{user_id}/organisation/domains")
    suspend fun getAllDomains(
        @Path("user_id") userID: Int
    ): List<String>
    @GET("/user/{user_id}/organisation/orgskills")
    suspend fun getAllOrgSkills(
        @Path("user_id") userID: Int
    ): List<String>

    @PATCH("/user/{user_id}/organisation/collab-tools")
    suspend fun collabTools(
        @Path("user_id") userID: Int,
        @Body request: CollabToolsRequest
    )

    @PUT("/user/{user_id}/organisation/details")
    suspend fun saveOrgDetails(
        @Path("user_id") userID: Int,
        @Body saveOrgDetails: SaveOrgDetails
    )

    @GET("/user/{user_id}/organisation/download")
    suspend fun getEmployeeSheet(
        @Path("user_id") userID: Int
    ) : ResponseBody
    @Multipart
    @POST("/user/{user_id}/organisation/upload")
    suspend fun uploadEnmployeeSheet(
        @Path("user_id") userID: Int,
        @Part file: MultipartBody.Part
    )

    @Multipart
    @PATCH("/user/{user_id}/organisation/profile-photo")
    suspend fun uploadOrgProfilePhoto(
        @Path("user_id") userID: Int,
        @Part file: MultipartBody.Part
    )

    @POST("/user/{user_id}/organisation/addskills")
    suspend fun addOrgSkill(
        @Path("user_id") userID: Int,
        @Body request: OrgAddSkillRequest
    )

    @DELETE("/user/{user_id}/organisation/org-account-operations")
    suspend fun deleteOrgAccount(
        @Path("user_id") userID: Int,
        @Query("type") type: String
    ): OrgAccountSettingResponse
}

















/*
package com.tft.selfbest.network

import com.tft.selfbest.data.entity.SelfBestNotification
import com.tft.selfbest.models.*
import com.tft.selfbest.models.calendarEvents.EventDetail
import com.tft.selfbest.models.mycourse.*
import com.tft.selfbest.models.notifications.NotificationResponse
import com.tft.selfbest.models.overview.*
import com.tft.selfbest.models.suggestedApps.AppDetail
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

interface SelfBestApi {

    @GET("user/getnotification")
    suspend fun getNotificationInfo(
        @Query("Email") email: String,
        @Query("Type") type: String,
        @Query("loc") rimeZone: String,
    ): SelfBestNotification

    @GET("user/getobservation")     //replace this with set observation
    suspend fun getObservation(
        @Query("Email") email: String,
        @Query("Url") type: String?,
    ): DistractedObservationResponse

    @GET("user/{user_id}/dashboard/distraction")
    suspend fun getDistractedApps(@Path("user_id") userID: Int): DistractionAppResponse

    @GET("user/{user_id}/dashboard/profile")
    suspend fun getProfile(@Path("user_id") userID: Int,): ProfileResponse

    @GET("user/{user_id}/dashboard/profile")
    suspend fun getProfile(
        @Path("user_id") userID: Int,
        @Query("platform") platform: String
    ): ProfileResponse

    @GET("sheet/job-positions")
    suspend fun getJobs(): List<String>

    @GET("sheet/onetcenter/skills?q=&&organisation_id=")
    suspend fun getAllSkills(): List<String>

    @POST("login?loc=Asia/Kolkata")
    suspend fun login(@Body loginRequest: LoginRequest): LogedInData

    @POST("user/{user_id}/setdeviceobservation")
    suspend fun sendDistractedAppUsage(
        @Path("user_id") userID: Int,
        @Body data: SendUsedDistractedApps,
    )

    @POST("user/{user_id}/getgoday/getgohourdetails")
    suspend fun getGoHourDetails(
        @Path("user_id") userID: Int,
        @Query("loc") timeZone: String,
    ): GoHourResponse

    @POST("user/{user_id}/device-observation")
    suspend fun sendDeviceObservation(
        @Path("user_id") userID: Int,
        @Body data: DeviceObservationRequest,
    ): DeviceObservationResponse

    @POST("user/{user_id}/device-observations")
    suspend fun sendOfflineDeviceObservations(
        @Path("user_id") userID: Int,
        @Body deviceObservationsData: SendOfflineDeviceObservations,
    ): DeviceObservationResponse

    @GET("user/{user_id}/getgoday/status")
    suspend fun getGoHourStatus(
        @Path("user_id") userID: Int,
        @Query("location") location: String,
    ): GoHourResponse

    @GET("user/{user_id}/getgoday/status")
    suspend fun getGoHourStatusWithStartTime(
        @Path("user_id") userID: Int,
        @Query("location") location: String,
        @Query("startTime") startTime: String,
    ): GoHourStatusResponse

    @POST("google/auth?location=Asia/Calcutta")
    suspend fun loginViaGoogle(@Body googleLoginRequest: GoogleLoginRequest): LogedInData

    @GET("linkedin/auth")
    suspend fun loginViaLinked(
        @Query("code") code: String,
        @Query("location") location: String,
        @Query("type") type: String,
    ): LogedInData

    @GET("/user/{user_id}/getgoday/activity")
    suspend fun overViewActivity(
        @Path("user_id") userID: Int,
        @Query("loc") location: String,
    ): OverViewActivityResponse

    @GET("/user/{user_id}/getgoday/level")
    suspend fun overViewLevel(
        @Path("user_id")
        userID: Int,
    ): OverViewLevelResponse

    @GET("/user/{user_id}/dashboard/overviewtop")
    suspend fun getOverViewDistractedTime(
        @Path("user_id") userID: Int,
        @Query("location") location: String,
    ): OverViewDistractedResponse

    @GET("/user/{user_id}/getgoday/stats")
    suspend fun getOverviewCompletedTime(@Path("user_id") userID: Int): OverViewCompletedResponse

    @GET("/user/{user_id}/getgoday/suggested-app")
    suspend fun getSuggestedApps(@Path("user_id") userID: Int): List<AppDetail>

    @POST("/user/{user_id}/getgoday/suggested-app")
    suspend fun addInSuggestedAppList(
        @Path("user_id") userID: Int,
        @Body appDetail: AppDetail,
    ): Int

    @GET("user/{user_id}/courses")
    suspend fun getCourses(@Path("user_id") userID: Int): OverviewCourses

    @GET("/user/{user_id}/calendar/events")
    suspend fun getUpcomingEvents(
        @Path("user_id") userID: Int,
        @Query("location") location: String,
        @Query("day") day: Int,
        @Query("month") month: String,
        @Query("year") year: Int,
    ): List<EventDetail>

    @POST("/user/{user_id}/calendar/event")
    suspend fun addOneDayEvent(
        @Path("user_id") userID: Int,
        @Body eventDetail: EventDetail,
    )

    @GET("/user/{user_id}/dashboard/getnotification")
    suspend fun getNotification(@Path("user_id") userID: Int): NotificationResponse

    @GET("/sheet/personalitymap")
    suspend fun getPersonality(): List<String>

    @Multipart
    @PATCH("/user/profile-photo")
    suspend fun updateProfilePicture(
        @Part image: MultipartBody.Part,
        @Part("userID") userID: RequestBody,
    ): ProfileImageResponse

    @GET("/user/{user_id}/calendar/disconnect")
    suspend fun disConnectCalendar(@Path("user_id") userID: Int)

    @GET("/user/{user_id}/calendar/connect")
    suspend fun connectCalendar(
        @Path("user_id") userID: Int,
        @Query("code") code: String,
        @Query("location") location: String,
        @Query("redirect_url") redirectUrl: String,
        @Query("calendar_type") type: String,
    )

    @GET("/user/{user_id}/managers")
    suspend fun getManagers(@Path("user_id") userID: Int): List<ManagerEmails>

    @GET("/user/{user_id}/getgoday/stats")
    suspend fun observations(
        @Path("user_id") userID: Int,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
    ): ObservationsResponse

    @GET("/sheet/onetcenter/skills")
    suspend fun getSkills(): List<String>

    @PUT("/user/{user_id}/detail")
    suspend fun saveProfileChanges(
        @Path("user_id") userID: Int,
        @Body profileChangesData: ProfileChangesData
    )

    @POST("/user/{user_id}/getgoday/timesheet-achievement-mail")
    suspend fun sendTimeSheetToManager(
        @Path("user_id") userID: Int,
        @Body profileChangesData: SelectedTimeSheetData
    )

    @GET("/user/{user_id}/courses")
    suspend fun getEnrolledCourses(
        @Path("user_id") userID: Int
    ): EnrolledCourseResponse

    @GET("/user/{user_id}/course/suggested?skill=GO&platform=0")
    suspend fun getSuggestedCourses(
        @Path("user_id") userID: Int,
        @Query("skill") skill: String,
        @Query("platform") platform: Int
    ): SuggestedCourseResponse

    @POST("/user/{user_id}/course/search")
    suspend fun getSearchResult(
        @Path("user_id") userID: Int,
        @Body filterSearchCourse: FilterSearchCourse
    ): List<SuggestedCourse>

    @POST("/user/{user_id}/course")
    suspend fun addCourse(
        @Path("user_id") userID: Int, @Body addCourse: AddCourse
    )

    @Multipart
    @POST("/user/{user_id}/course/upload-certificate")
    suspend fun uploadCertificate(
        @Path("user_id") userID: Int,
        @Part certificate: MultipartBody.Part,
        @Part("CourseId") courseId: RequestBody
    )

    //Activity Log screen
    @GET("/user/{user_id}/activity-logs?type=Web&event=daily")
    suspend fun getActivityLogs(
        @Path("user_id") userID: Int,
        @Query("type") type: String,
        @Query("event") event : String
    ) : List<ActivityLogValues>

    //Distraction
    @POST("/user/{user_id}/dashboard/distraction")
    suspend fun addDistraction(
        @Path("user_id") userID: Int,
        @Body addDistraction : AddDistraction
    )

    @DELETE("/user/{user_id}/dashboard/distraction/{id}")
    suspend fun deleteDistraction(
        @Path("user_id") userID: Int,
        @Path("id") id: Int
    )

    @PATCH("/user/{user_id}/dashboard/toggledistraction")
    suspend fun toggleDistraction(
        @Path("user_id") userID: Int,
        @Body togDist : ToggleDistraction
    )

    @GET("/user/{user_id}/getgohour")
    suspend fun getGoHour(
        @Path("user_id") userID: Int
    ) : GetGoHourResponse

    @POST("/user/{user_id}/getgohour/get-started?loc=Asia/Calcutta")
    suspend fun getStarted(
        @Path("user_id") userID: Int,
        @Body startTime : StartTime
    )

    @POST("/user/{user_id}/getgohour/start")
    suspend fun start(
        @Path("user_id") userID: Int,
        @Query("loc") loc : String,
        @Body startStatus: StartBody
    )

    @POST("/user/{user_id}/getgohour/pause?loc=Asia/Calcutta")
    suspend fun pause(
        @Path("user_id") userID: Int,
//        @Query("loc") loc: String,
        @Body startTime: StartTime
    )

    @PATCH("/user/{user_id}/getgohour/end")
    suspend fun end(
        @Path("user_id") userID: Int,
        @Query("loc") loc : String,
        @Body endTime : EndTime
    )

    @PATCH("/user/{user_id}/getgohour/reset")
    suspend fun reset(
        @Path("user_id") userID: Int,
        @Query("loc") loc:String,
        @Body startTime : StartTime
    ): Reset

    @PATCH("/user/{user_id}/getgohour/resume?loc=Asia/Calcutta")
    suspend fun resume(
        @Path("user_id") userID: Int,
//        @Query("loc") loc: String,
        @Body EndTime: EndTime
    ): TotalPauseTime

    @PATCH("user/{user_id}/getgohour/timeinterval")
    suspend fun timeInterval(
        @Path("user_id") userID: Int,
        @Query("loc") loc:String,
        @Body timeInterval: TimeInterval
    )

    @GET("/user/{user_id}/getgohour/activities?location=Asia/Calcutta")
    suspend fun getActivity(
        @Path("user_id") userID: Int,
    ): ActivityResponse

    @GET("/user/{user_id}/getgohour/activities")
    suspend fun getChartData(
        @Path("user_id")userID :Int,
        @Query("location") location:String
    ): GetChartData

    @PUT("/user/{user_id}/getgohour/complete")
    suspend fun inputProgressComplete(
        @Path("user_id") userID:Int,
        @Body inputData : InputData
    )

    @POST("/user/{user_id}/detail")
    suspend fun saveData(
        @Path("user_id") userID: Int,
        @Body signUpData: SignUpDetail
    )

    @GET("https://googlebot.self.best/userresponses/helpsession-details")
    suspend fun getQuery(
        @Query("email") email: String
    ): List<QueryResponse>

    @GET("https://googlebot.self.best/userresponses/helpsession-details-expert")
    suspend fun getAnsweredQuery(
        @Query("email") email: String
    ): List<QueryAnsweredResponse>

    @FormUrlEncoded
    @POST("https://googlebot.self.best/userresponses/update-status/")
    suspend fun updateStatus(
        @Field("id") id: Int,
        @Field("status") status: Int
    ): UpdationBody

    @GET("/user/{user_id}/skill-recommendation")
    suspend fun getRecommendation(
        @Path("user_id") userID: Int,
        @Query("q") q: String
    ): List<String>

    @Multipart
    @POST("/user/{user_id}/resume-upload")
    suspend fun uploadResume(
        @Path("user_id") userID: Int,
        @Part resume: MultipartBody.Part,
    ):UploadResumeResponse

    @GET("/user/{user_id}/getgoday/timeline?location=Asia/Kolkata")
    suspend fun getTimelineData(
        @Path("user_id") userID: Int,
    ): List<ActivityTimelineResponse>

    @GET("/user/{user_id}/getgoday/pointsboard")
    suspend fun getPoints(
        @Path("user_id") userID: Int,
        @Query("location") loc: String,
        @Query("event") event: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<PointGraphResponse>

    @GET("user/{user_id}/organisation/user-requests?status=accepted,pending,rejected")
    suspend fun userRequest(
        @Path("user_id") userID: Int
    ):List<UserRequestResponse>
}*/
