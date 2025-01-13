package pt.ipt.dam.trabalho_final_dam.model

import com.google.gson.annotations.SerializedName

data class Fotos (
    @SerializedName("fotos") val fotos: List<Foto>?
)

