package pt.ipt.dam.trabalho_final_dam.model

import com.google.gson.annotations.SerializedName

data class Utilizador (
    @SerializedName("email") val Email: String?,
    @SerializedName("password") val Password: String?,
    @SerializedName("nome") val Nome: String?
//    @SerializedName("id") val id: Int
)