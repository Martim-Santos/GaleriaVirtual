package pt.ipt.dam.galeria_virtual.base64

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class Base64 {

    // Convert Bitmap to Base64 with compression
    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream) // Use JPEG for lossy compression
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Compress a Base64 image to fit within a size limit (in KB)
    fun compressBase64Image(base64Image: String, maxSizeInKB: Int): String {
        // Decode Base64 string to Bitmap
        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        // Resize the Bitmap to reduce initial size
        val resizedBitmap = resizeBitmap(bitmap, 500, 500) // Resize to 800x800 pixels (adjust as needed)

        var quality = 100 // Start with maximum quality
        var compressedBitmap: ByteArrayOutputStream

        do {
            compressedBitmap = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, compressedBitmap)

            // Reduce quality by 10% in each iteration
            quality -= 10
        } while (compressedBitmap.size() / 1024 > maxSizeInKB && quality > 10) // Check size and quality limits

        // Convert the compressed ByteArray back to Base64
        val compressedBytes = compressedBitmap.toByteArray()
        return Base64.encodeToString(compressedBytes, Base64.DEFAULT)
    }

    // Resize a Bitmap to a target width and height
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val aspectRatio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int

        if (width > height) {
            newWidth = maxWidth
            newHeight = (maxWidth / aspectRatio).toInt()
        } else {
            newHeight = maxHeight
            newWidth = (maxHeight * aspectRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

}
