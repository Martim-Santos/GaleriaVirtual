package pt.ipt.dam.trabalho_final_dam.fragmentos

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import pt.ipt.dam.galeria_virtual.databinding.Fragmento1Binding
import pt.ipt.dam.trabalho_final_dam.base64.Base64
import pt.ipt.dam.trabalho_final_dam.model.Foto
import pt.ipt.dam.trabalho_final_dam.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class fragmento1 : Fragment() {

    private var _binding: Fragmento1Binding? = null
    private val binding get() = _binding!!

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    private lateinit var base64: Base64

    companion object {
        private const val TAG = "CameraXFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout and initialize binding
        _binding = Fragmento1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, 101)
        }

        binding.imageCaptureButton.setOnClickListener {
            takePhoto()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

//    private fun takePhoto() {
//        val imageCapture = imageCapture ?: return
//
//        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.UK)
//            .format(System.currentTimeMillis())
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TrabalhoFinalDAM-Images")
//            }
//        }
//
//        val outputOptions = ImageCapture.OutputFileOptions
//            .Builder(
//                requireContext().contentResolver,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                contentValues
//            ).build()
//
//        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
//                }
//
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val msg = "Photo capture succeeded: ${output.savedUri}"
//                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, msg)
//                }
//            })
//    }
//
//            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                val msg = "Photo capture succeeded: ${output.savedUri}"
//                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
//                Log.d(TAG, msg)
//            }
//        })
//    }

private fun takePhoto() {
   val imageCapture = imageCapture ?: return

    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.UK)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            requireContext().contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

    imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val uri = output.savedUri
                if (uri != null) {
                    processImage(uri) // Process the image for API upload
                } else {
                   Toast.makeText(requireContext(), "Photo capture failed.", Toast.LENGTH_SHORT).show()
                }
            }
        })
}

private fun processImage(uri: Uri) {
    try {
        // Load the image as a Bitmap
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Convert Bitmap to Base64
        val base64String = base64.convertBitmapToBase64(bitmap)

        // Send Base64 string to API
        sendImageToApi(base64String)

    } catch (e: Exception) {
        Log.e(TAG, "Error processing image", e)
    }
}

    private fun sendImageToApi(base64String: String) {
    val apiService = RetrofitInitializer().fotoService() // Adjust this to your API service class

    val call = apiService.addFoto("email", base64String, FILENAME_FORMAT) // Define this endpoint in your Retrofit service
    call.enqueue(object : Callback<Foto> {
        override fun onResponse(call: Call<Foto>, response: Response<Foto>) {
            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Imagem salvada com sucesso.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Upload failed: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Foto>, t: Throwable) {
            Log.e(TAG, "Upload failed: ${t.message}", t)
        }

    })
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cameraExecutor.shutdown()
    }
}
