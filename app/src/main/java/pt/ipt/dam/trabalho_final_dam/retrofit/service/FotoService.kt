package pt.ipt.dam.trabalho_final_dam.retrofit.service

import retrofit2.Call
import pt.ipt.dam.trabalho_final_dam.model.Foto
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST


interface FotoService {
    @GET("https://api.sheety.co/44999724e36c8f60a4a0444b7f325fbd/projetoFinalDam/fotos")
    fun listFoto(): Call<List<Foto>>

    @FormUrlEncoded
    @POST("https://api.sheety.co/44999724e36c8f60a4a0444b7f325fbd/projetoFinalDam/fotos")
    fun addFoto(@Field("Email") Email: String?,
                @Field("Foto") Foto: String?,
                @Field("Descricao") Descricao: String?): Call<Foto>

    @DELETE("https://api.sheety.co/44999724e36c8f60a4a0444b7f325fbd/projetoFinalDam/fotos/{id}")
    fun deleteFoto(@Field("id") id: Int): Call<Foto>

    @PATCH("https://api.sheety.co/44999724e36c8f60a4a0444b7f325fbd/projetoFinalDam/fotos/{id}")
    fun editFoto(@Field("Descricao") Descricao: String?): Call<Foto>

}