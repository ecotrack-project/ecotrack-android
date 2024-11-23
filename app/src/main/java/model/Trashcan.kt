package model

data class Trashcan (
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val fillinglevel : Int,
    val batteryLevel: Int,
    val trashType: String,

)