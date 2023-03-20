package com.tft.selfbest.data.entity


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "distractedApp", primaryKeys = ["id"])
data class DistractedApp(
    @SerializedName("id")
    val id: Int,
    @SerializedName("url")
    val url: String?,
    @SerializedName("state")
    var state: Boolean?,
){
    /*override fun toString(): String {
        return "$id $name $site $state $userid"
    }*/

    override fun toString(): String {
        return "$id $url $state"
    }
}