package model


data class ReportApiResponse(
    val response: List<Trashcan>,
    val errorRTO: String? // To handle the optional "errorRTO" field
)

data class Report(
    val userEmail: String,
    val trashcanId: Long,
    val description: String
)