package pt.ipt.dam.trabalho_final_dam.base64

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class Base64 {

    // Converter Bitmap para Base64 com compressão
    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            30,
            byteArrayOutputStream
        )
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    // Converter Base64 para Bitmap com decode
    fun convertBase64toBitmap(base64String: String): Bitmap {
        val decodedString: ByteArray =
            android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

}
