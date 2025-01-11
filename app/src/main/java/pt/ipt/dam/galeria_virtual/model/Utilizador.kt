package pt.ipt.dam.galeria_virtual.model

import com.google.gson.annotations.SerializedName

data class Utilizador (
    @SerializedName("email") val Email: String?,
    @SerializedName("password") val Password: String?,
    @SerializedName("nome") val Nome: String?
)