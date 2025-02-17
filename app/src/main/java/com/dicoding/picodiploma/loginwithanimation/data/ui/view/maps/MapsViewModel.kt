package com.dicoding.picodiploma.loginwithanimation.data.ui.view.maps

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository

class MapsViewModel(private val repository: UserRepository): ViewModel() {
    fun getStoriesWithLocation() = repository.getStoriesWithLocation()
}