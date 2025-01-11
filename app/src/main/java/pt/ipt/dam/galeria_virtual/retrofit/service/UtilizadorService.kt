package pt.ipt.dam.galeria_virtual.retrofit.service

import pt.ipt.dam.galeria_virtual.model.Utilizador
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UtilizadorService {
    @FormUrlEncoded
    @POST("https://api.sheety.co/44999724e36c8f60a4a0444b7f325fbd/projetoFinalDam/logins")
    fun addUser(@Field("email") Email: String?,
                @Field("password") Password: String?,
                @Field("nome") Nome: String?): Call<Utilizador>
}