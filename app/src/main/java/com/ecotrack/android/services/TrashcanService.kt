package com.ecotrack.android.services
import model.TrashcanApiResponse
import retrofit2.Call
import retrofit2.http.GET


interface TrashcanService {

    /**
     * Gets all the trashcans from the server.
     *
     * @return A Call object encapsulating the server's response.
     */
    @GET("/trashcan/list")
    fun getAllTrashcans(): Call<TrashcanApiResponse>


}
