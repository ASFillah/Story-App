package com.dicoding.picodiploma.loginwithanimation.data.ui.view.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.remote.response.ListStoryItem

@Suppress("DEPRECATION", "SameParameterValue")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
    }

    private fun setupAction() {
        val storyItem = intent.getParcelableExtra<ListStoryItem>(DATA_KEY)
        if(storyItem == null){
            showLoading(true)
        }else{
            showLoading(false)
            binding.apply{
                tvItemName.text = storyItem.name
                tvItemDescription.text = storyItem.description
                Glide.with(this@DetailActivity)
                    .load(storyItem.photoUrl)
                    .into(imgItemPhoto)
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val DATA_KEY = "story_data"
    }
}