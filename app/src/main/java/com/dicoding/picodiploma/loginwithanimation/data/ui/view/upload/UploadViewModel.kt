package com.dicoding.picodiploma.loginwithanimation.data.ui.view.upload

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import java.io.File

class UploadViewModel(private val repository: UserRepository) : ViewModel(){
    fun addStories(file: File, description: String) = repository.addStories(file, description)
}