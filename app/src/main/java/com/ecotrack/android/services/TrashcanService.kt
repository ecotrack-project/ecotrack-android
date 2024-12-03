package com.ecotrack.android.services
import model.TrashcanApiResponse
import retrofit2.Call
import retrofit2.http.GET


interface TrashcanService {

    @GET("/api/trashcans")
    fun getAllTrashcans(): Call<TrashcanApiResponse>


}
