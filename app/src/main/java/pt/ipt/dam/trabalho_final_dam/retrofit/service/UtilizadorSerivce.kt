package pt.ipt.dam.trabalho_final_dam.retrofit.service


import pt.ipt.dam.trabalho_final_dam.model.Utilizador
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface UtilizadorSerivce {
    @GET("https://api.sheety.co/44999724e36c8f60a4a0444b7f325fbd/projetoFinalDam/logins")
    fun listUser(): Call<List<Utilizador>>

    @POST("https://api.sheety.co/44999724e36c8f60a4a0444b7f325fbd/projetoFinalDam/logins")
    fun addUser(@Field("email") Email: String?,
                @Field("password") Password: String?,
                @Field("nome") Nome: String?): Call<Utilizador>
}