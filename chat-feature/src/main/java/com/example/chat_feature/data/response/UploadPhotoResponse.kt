package com.example.chat_feature.data.response

import com.google.gson.annotations.SerializedName

data class UploadPhotoResponse(
    @SerializedName("image_link")
    val imageLink:String,
    @SerializedName("status")
    val statusCode:Int
)

