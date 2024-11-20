package services

import model.Trashcan
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET

class TrashcanService(private val retrofit: Retrofit) {

    @GET("/trashcan/list")
    fun getAllTrashcans(): Call<List<Trashcan>> {
        return retrofit.create(TrashcanService::class.java).getAllTrashcans()
    }
}