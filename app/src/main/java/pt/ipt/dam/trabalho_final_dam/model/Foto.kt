package pt.ipt.dam.trabalho_final_dam.model

import com.google.gson.annotations.SerializedName


data class Foto(
    @SerializedName("email") val Email: String?,
    @SerializedName("foto") val Foto: String?,
    @SerializedName("descricao") var Descricao: String?
)
