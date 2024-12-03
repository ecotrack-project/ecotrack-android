package model


data class TrashcanApiResponse(
    val response: List<Trashcan>,
    val errorRTO: String? // To handle the optional "errorRTO" field
)

data class Trashcan(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val fillingLevel: Int,
    val batteryLevel: Int,
    val trashType: String
)
