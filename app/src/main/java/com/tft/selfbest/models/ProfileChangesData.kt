package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

data class ProfileChangesData(
    @SerializedName("AvailabiltyEndTime")
    var availabiltyEndTime: String,
    @SerializedName("AvailabiltyStartTime")
    var availabiltyStartTime: String,
    @SerializedName("CustomPersonalities")
    var customPersonalities: List<Any>,
    @SerializedName("Experience")
    var experience: Float,
    @SerializedName("Firstname")
    var firstname: String,
    @SerializedName("Gender")
    var gender: String,
    @SerializedName("GetGoDays")
    var getGoDays: List<Any>,
    @SerializedName("IsAccountabilityPartner")
    var isAccountabilityPartner: Boolean,
    @SerializedName("Lastname")
    var lastName: String,
    @SerializedName("Location")
    var location: String = "Asia/Calcutta",
    @SerializedName("Occupation")
    var occupation: String,
    @SerializedName("PersonalityType")
    var personalityType: String,
    @SerializedName("ShowLeaderBoard")
    var ShowLeaderBoard: Boolean,
    @SerializedName("Skills")
    var Skills: LinkedTreeMap<String, Int>,
    @SerializedName("WorkEndTime")
    var workEndTime: String,
    @SerializedName("WorkStartTime")
    var workStartTime: String,
    @SerializedName("WorkingDays")
    var workingDays: List<String>,
    @SerializedName("accountabilitypartnerEmail")
    var accountabilitypartnerEmail: String = ""

)