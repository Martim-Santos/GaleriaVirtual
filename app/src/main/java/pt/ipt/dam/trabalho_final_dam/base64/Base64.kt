package pt.ipt.dam.trabalho_final_dam.base64

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.util.Base64


class Base64 {

    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}
