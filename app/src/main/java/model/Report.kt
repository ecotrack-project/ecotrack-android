package model

import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("user_email")
    val userEmail: String,

    @SerializedName("trashcan_id")
    val trashcanId: Long,

    @SerializedName("description")
    val description: String
)
