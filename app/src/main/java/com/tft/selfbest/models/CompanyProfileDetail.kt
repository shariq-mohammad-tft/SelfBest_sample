package com.tft.selfbest.models

import com.google.gson.annotations.SerializedName

data class CompanyProfileDetail(
    @SerializedName("Detail")
    val detail: CompanyDetails,
    @SerializedName("SimplifyPathTools")
    val simplifyPathTools: IntegratedTools
)

data class CompanyDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("Url")
    val url: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Status")
    val status: String,
    @SerializedName("Size")
    val size: Long,
    @SerializedName("LinkedIn")
    val linkedIn: String,
    @SerializedName("Domain")
    val domain: String,
    @SerializedName("EmailDomain")
    val emailDomain: String,
    @SerializedName("GoogleMeetUrl")
    val googleMeetUrl: String,
    @SerializedName("Skills")
    val skill: List<String>? = listOf(),
    @SerializedName("Distractions")
    val distractions: List<String>? = listOf(),
    @SerializedName("NewOrg")
    val newOrg: Boolean,
    @SerializedName("SlackWorkspaceID")
    val slackId: String,
    @SerializedName("TypeOfBot")
    val botType: String,
    @SerializedName("Calendar")
    val calendar: String,
    @SerializedName("WorkDen")
    val workDen: Boolean,
    @SerializedName("SimplifyPath")
    val simplifyPath: Boolean,
    @SerializedName("SolutionPoint")
    val solutionPoint: Boolean,
    @SerializedName("Image")
    val image: String,
    @SerializedName("Password")
    val password: String,
    @SerializedName("CreatedAt")
    val createdAt: String,
    @SerializedName("Deleted")
    val deleted: Boolean,
    @SerializedName("DeleteAccountStatus")
    val deleteAccountStatus: String,
    @SerializedName("DeleteAccountDataStatus")
    val deleteAccountDataStatus: String,
    @SerializedName("DeactivateAccountStatus")
    val deactivateAccountStatus: String,
    @SerializedName("ExternalExpertPool")
    val externalExpertPool: Boolean
)