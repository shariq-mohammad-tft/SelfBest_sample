package com.tft.selfbest.models


import com.google.gson.annotations.SerializedName

data class ProfileData(

    @SerializedName("Contact")
    val contact: String?,
    @SerializedName("accountabilitypartner")
    val accountabilitypartner: Boolean?,
    @SerializedName("CreatedAt")
    val createdAt: String?,
    @SerializedName("Dob")
    val dob: String?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("ExistingGoals")
    val existingGoals: List<String>?,
    @SerializedName("ExtensionUsedAt")
    val extensionUsedAt: String?,
    @SerializedName("FirstName")
    val firstName: String?,
    @SerializedName("Gender")
    var gender: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("ImportConnectionsLinkedIn")
    val importConnectionsLinkedIn: Boolean?,
    @SerializedName("ImportGoogleCalendar")
    val importGoogleCalendar: Boolean?,
    @SerializedName("ImportOutlookCalendar")
    val importOutlookCalendar: Boolean?,
    @SerializedName("Interests")
    val interests: List<String>?,
    @SerializedName("is_org_admin")
    val isOrgAdmin: Boolean?,
    @SerializedName("Isadmin")
    val isadmin: Boolean?,
    @SerializedName("Isapproved")
    val isapproved: Int?,
    @SerializedName("JiraId")
    val jiraId: String?,
    @SerializedName("LastName")
    val lastName: String?,
    @SerializedName("Lastlogin")
    val lastlogin: String?,
    @SerializedName("Lastloginworkinghour")
    val lastloginworkinghour: Boolean?,
    @SerializedName("Learning")
    val learning: List<Learning>?,
    @SerializedName("linkedin_profile_url")
    val linkedinProfileUrl: String?,
    @SerializedName("NoOfExperience")
    val noOfExperience: Number?,
    @SerializedName("Occupation")
    val occupation: String?,
    @SerializedName("Password")
    val password: String?,
    @SerializedName("PersonalityType")
    val personalityType: String?,
    @SerializedName("RejectedCount")
    val rejectedCount: Int?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("Skillenhanced")
    val skillenhanced: Int?,
    @SerializedName("Skills")
    var skills: Any?,
    @SerializedName("PendingSkills")
    var pendingskills: Any?,
    @SerializedName("TimeZone")
    val timeZone: String?,
    @SerializedName("TypicalProcrastinatorTrigger")
    val typicalProcrastinatorTrigger: List<String>?,
    @SerializedName("UpdatedAt")
    val updatedAt: String?,
    @SerializedName("Working")
    val working: List<ProfileWorkingData>,
    @SerializedName("OrganisationName")
    val organisationName: String,
    @SerializedName("CustomPersonality")
    val customPersonality: List<String>
)