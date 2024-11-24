package services
import model.Report
import retrofit2.http.POST
import retrofit2.Call


interface ReportService {

    @POST("/report")
    fun createReport(report: Report): Call<Report> // Create a new report
}