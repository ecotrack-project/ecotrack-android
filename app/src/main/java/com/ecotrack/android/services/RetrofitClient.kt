package com.ecotrack.android.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"

    // Retrofit instance condivisa
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servizio TrashcanService
    val trashcanService: TrashcanService by lazy {
        retrofit.create(TrashcanService::class.java)
    }

    // Servizio ReportService
    val reportService: ReportService by lazy {
        retrofit.create(ReportService::class.java)
    }

}
