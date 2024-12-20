package pt.ipt.dam.trabalho_final_dam.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pt.ipt.dam.trabalho_final_dam.retrofit.service.UtilizadorService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    val host = "https://api.sheety.co/44999724e36c8f60a4a0444b7f325fbd/projetoFinalDam/"

    private val gson: Gson = GsonBuilder().setLenient().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun utilizadorService() = retrofit.create(UtilizadorService::class.java)
}
