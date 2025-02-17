package com.dicoding.picodiploma.loginwithanimation.data.ui.view.upload

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.data.ui.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.data.ui.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityUploadBinding
import com.dicoding.picodiploma.loginwithanimation.util.getImageUri
import com.dicoding.picodiploma.loginwithanimation.util.uriToFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<UploadViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            binding.btnGalery.setOnClickListener { startGalery() }
            binding.btnCamera.setOnClickListener { startCamera() }
            binding.btnUpload.setOnClickListener { addStory() }
        }
    }

    private fun startGalery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){ uri: Uri? ->
        if(uri != null){
            currentImageUri = uri
            showImage()
        }else{
            Log.d("Photo Picker", "Tidak ada media yang dipilih")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherCamera.launch(currentImageUri)
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun File.reduceFileImage(): File {
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        val maximalSize = 1000000
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > maximalSize)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun showImage(){
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun addStory(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.etDescription.text.toString()

            viewModel.addStories(imageFile, description).observe(this){story ->
                if(story != null) {
                    when (story) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            showToast(story.data.message)
                            showLoading(false)
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        is ResultState.Error -> {
                            showToast(story.error.toString())
                            showLoading(false)
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.warning_empty))
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}