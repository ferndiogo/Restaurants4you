package com.dam.restaurants4you.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import android.widget.Button
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import com.dam.restaurants4you.R
import java.text.SimpleDateFormat
import java.util.Locale

class CamaraActivity : AppCompatActivity() {

    private lateinit var imgPath: String

    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    private var imageCapture: ImageCapture? = null

    var idAux: Int? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // acessar á view
        setContentView(R.layout.camera)

        // recebe o id para saber a que restaurante será associada a foto
        idAux = intent.getIntExtra("idR", -1)

        //inicia a câmara
        startCamera()

        // configura os listeners para tirar a foto
        val tirarFoto = findViewById<Button>(R.id.tirarFoto)
        tirarFoto.setOnClickListener { tirarFoto() }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun tirarFoto() {
        // Obtém uma referência estável do caso de uso de captura de imagem modificável
        val imageCapture = imageCapture ?: return

        // Cria um nome com o registro da data e hora e uma entrada do MediaStore
        val name =
            SimpleDateFormat(FILENAME_FORMAT, Locale.FRANCE).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                val path = "Pictures/Restaurants4You"
                put(MediaStore.Images.Media.RELATIVE_PATH, path)
            }
        }

        // Cria objeto de saída que contém o arquivo + metadados
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
                contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
            ).build()

        // Configura o image capture listener, que é acionado após a foto ser tirada
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(R.string.app_name.toString(), "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    imgPath = output.savedUri.toString()


                    val intent = Intent(this@CamaraActivity, RestaurantesActivity::class.java)
                    intent.putExtra("pathImg", imgPath)
                    intent.putExtra("idR", idAux)
                    startActivity(intent)


                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Log.d(R.string.app_name.toString(), msg)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                    val viewFinder = findViewById<PreviewView>(R.id.viewFinder)
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Seleciona a câmara traseira como default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Desvincula os casos de uso antes de revincular
                cameraProvider.unbindAll()

                // Vincula casos de uso à câmera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )


            } catch (exc: Exception) {
                Log.e(R.string.app_name.toString(), "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
