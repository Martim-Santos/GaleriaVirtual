package pt.ipt.dam.trabalho_final_dam.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pt.ipt.dam.trabalho_final_dam.retrofit.service.UtilizadorSerivce
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    val host = "http://ram.ipt.pt/"
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    fun utilizadorSerivce() = retrofit.create(UtilizadorSerivce::class.java)
}
