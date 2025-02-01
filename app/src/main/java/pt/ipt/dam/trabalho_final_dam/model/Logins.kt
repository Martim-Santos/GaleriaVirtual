package pt.ipt.dam.trabalho_final_dam.model

import com.google.gson.annotations.SerializedName

data class Logins (
    @SerializedName("logins") val logins: List<Utilizador>?
)