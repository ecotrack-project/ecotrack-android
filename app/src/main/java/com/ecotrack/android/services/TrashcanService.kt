package com.ecotrack.android.services
import model.Trashcan
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET


interface TrashcanService {

    @GET("/trashcan/list")
    fun getAllTrashcans(): Call<List<Trashcan>>

}
