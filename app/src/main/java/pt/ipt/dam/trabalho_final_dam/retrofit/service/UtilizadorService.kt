package pt.ipt.dam.trabalho_final_dam.retrofit.service


import pt.ipt.dam.trabalho_final_dam.model.Utilizador
import retrofit2.Call
import retrofit2.http.*

interface UtilizadorService {
    @GET("logins/")
    fun listUser(): Call<List<Utilizador>>

    @FormUrlEncoded
    @POST("logins/")
    fun addUser(@Field("email") Email: String?,
                @Field("password") Password: String?,
                @Field("nome") Nome: String?): Call<Utilizador>


}