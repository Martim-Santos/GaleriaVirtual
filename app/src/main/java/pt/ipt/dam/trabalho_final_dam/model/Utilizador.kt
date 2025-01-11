package pt.ipt.dam.trabalho_final_dam.model

import com.google.gson.annotations.SerializedName

data class Utilizador (
    @SerializedName("id") val Id: Int?,
    @SerializedName("email") val Email: String?,
    @SerializedName("password") val Password: String?,
    @SerializedName("nome") val Nome: String?
)