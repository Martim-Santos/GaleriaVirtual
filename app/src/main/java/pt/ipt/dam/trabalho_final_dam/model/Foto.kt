package pt.ipt.dam.trabalho_final_dam.model

import com.google.gson.annotations.SerializedName


data class Foto(
    @SerializedName("Email") val Email: String?,
    @SerializedName("Foto") val Foto: String?,
    @SerializedName("Descricao") val Descricao: String?
)
