package com.ecotrack.android.services

import model.Report
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportService {

    /**
     * Sends a report to the server.
     *
     * @param report The Report object containing the report details.
     * @return A Call object encapsulating the server's response.
     */
    @POST("/report")
    fun createReport(@Body report: Report): Call<Response<Report>> // Added @Body annotation for clarity
}
