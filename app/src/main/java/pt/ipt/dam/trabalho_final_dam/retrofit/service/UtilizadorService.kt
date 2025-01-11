package pt.ipt.dam.trabalho_final_dam.retrofit.service



import pt.ipt.dam.trabalho_final_dam.model.Logins
import pt.ipt.dam.trabalho_final_dam.model.Utilizador
import retrofit2.Call
import retrofit2.http.*

interface UtilizadorService {
    @GET("projetoFinalDam/logins")
    fun listUser(): Call<Logins>

    //@FormUrlEncoded
    @POST("projetoFinalDam/logins")
    fun addUser(@Body body : HashMap<String, Any>): Call<Utilizador>
    //fazer HashMap para receber os dados sem o "logins:"

    //fun addUser(@Field("email") Email: String?,
    //            @Field("password") Password: String?,
    //            @Field("nome") Nome: String?): Call<Utilizador>


}