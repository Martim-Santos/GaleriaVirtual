package pt.ipt.dam.trabalho_final_dam.fragmentos

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import pt.ipt.dam.trabalho_final_dam.databinding.Fragmento1Binding
import pt.ipt.dam.trabalho_final_dam.base64.Base64
import pt.ipt.dam.trabalho_final_dam.model.Foto
import pt.ipt.dam.trabalho_final_dam.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class fragmento1 : Fragment() {

    private var _binding: Fragmento1Binding? = null
    private val binding get() = _binding!!

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    private lateinit var base64: Base64

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val TAG = "CameraXFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS = mutableListOf(
            // import android.Manifest
            Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()

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

        base64 = Base64()

        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)


        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, 101)
        }
        // tratar do evento 'click' do botão
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

    //inicia a camera
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


    // Tira a fotografia
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
                        Toast.makeText(requireContext(), "Foto tirada com sucesso.", Toast.LENGTH_SHORT).show()
                        processImage(uri) // Process the image for API upload
                    } else {
                        Toast.makeText(requireContext(), "Não foi possível tirar a foto.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    // Recebe a imagem e turna o bitmap em base64
    private fun processImage(uri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val base64String = base64.convertBitmapToBase64(bitmap)


            sendImageToApi(base64String)

        } catch (e: Exception) {
            Log.e(TAG, "Error processing image", e)
        }
    }

    // manda a base64 da imagem para o API
    private fun sendImageToApi(base64String: String) {
        val apiService = RetrofitInitializer().fotoService()

        val email = sharedPreferences.getString("email", null) // Vai buscar o email do utilizador autenticado.

        if (email == null) {
            Toast.makeText(requireContext(), "Erro: Utilizador não logado.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val fotoData = hashMapOf<String,Any>(
            "email" to email,
            "foto" to base64String,
            "descricao" to currentDate.toString() // salva a descrição da foto como a data em que foi tirada.
        )

        val requestBody = hashMapOf<String, Any>(
            "foto" to fotoData
        )

        val call = apiService.addFoto(requestBody)
        call.enqueue(object : Callback<Foto> {
            override fun onResponse(call: Call<Foto>, response: Response<Foto>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Imagem salvada com sucesso.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Upload falhado: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Foto>, t: Throwable) {
                Log.e(TAG, "Upload falhado: ${t.message}", t)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cameraExecutor.shutdown()
    }
}
