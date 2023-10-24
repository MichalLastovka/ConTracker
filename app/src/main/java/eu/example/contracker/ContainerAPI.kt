package eu.example.contracker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface myAPI {
    @GET("api/container/auskunft/{container}")
    fun getMyContainer(@Path(value = "container") container: String): Call<ContainerData>
}