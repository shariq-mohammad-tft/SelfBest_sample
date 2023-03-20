package com.tft.selfbest.models.mycourse

import com.google.gson.annotations.SerializedName

data class EnrolledCourse(
    @SerializedName("Certificate")
    val certificate: String,
    @SerializedName("CertificateStatus")
    val certificateStatus: String,
    @SerializedName("Courseid")
    val courseId: String,
    @SerializedName("Courseimageurl")
    val courseImageUrl: String,
    @SerializedName("Courseurl")
    val courseUrl: String,
    @SerializedName("CreatedAt")
    val createdAt: String,
    @SerializedName("Duration")
    val duration: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Modules")
    val modules: Int,
    @SerializedName("Modulescompleted")
    val modulesCompleted: Int,
    @SerializedName("Name")
    val courseName: String,
    @SerializedName("Status")
    val status: Int,
    @SerializedName("Type")
    val type: String,
    @SerializedName("UpdatedAt")
    val updatedAt: String,
    @SerializedName("Userid")
    val userId: Int
)