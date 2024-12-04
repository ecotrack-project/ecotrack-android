package com.ecotrack.android.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"


    val instance: TrashcanService by lazy {

        // Build Retrofit with the customized OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(TrashcanService::class.java)
    }

    val reportService: ReportService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()             //.client(createOkHttpClient()) NON ESISTE INTERCEPTOR
            .create(ReportService::class.java)
    }

}
