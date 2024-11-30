package com.ecotrack.android.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val instance: TrashcanService by lazy {
        // Create HttpLoggingInterceptor
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs request and response bodies
        }

        // Build OkHttpClient with the interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging) // Attach the logging interceptor
            .build()

        // Build Retrofit with the customized OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Use the customized OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(TrashcanService::class.java)
    }
}
