package model

data class Trashcan (
    var latitude: Double,
    var longitude: Double,
    var fillinglevel : Int,
    var batterylevel: Int,
    var trashtype: String
)