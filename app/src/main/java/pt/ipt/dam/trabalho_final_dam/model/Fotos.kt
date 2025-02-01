package pt.ipt.dam.trabalho_final_dam.model

import com.google.gson.annotations.SerializedName

data class Fotos (
    @SerializedName("fotos") val fotos: List<Foto>?
) {
    fun filter(predicate: (Foto) -> Boolean): List<Foto> {
        return fotos?.filter(predicate) ?: emptyList()
    }

}

