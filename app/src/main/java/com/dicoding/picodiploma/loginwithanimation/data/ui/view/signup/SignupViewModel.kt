package com.dicoding.picodiploma.loginwithanimation.data.ui.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository

class SignupViewModel (private val repository: UserRepository) : ViewModel() {
    fun register(nama: String, email: String, password: String) = repository.register(nama, email, password)
}