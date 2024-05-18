package com.dicoding.picodiploma.loginwithanimation.data.ui.view.main

import android.content.Intent
import com.google.android.material.progressindicator.LinearProgressIndicator
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.ui.adapter.ListStoryItemAdapter
import com.dicoding.picodiploma.loginwithanimation.data.ui.adapter.LoadingStateAdapter
import com.dicoding.picodiploma.loginwithanimation.data.ui.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.data.ui.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.data.ui.view.upload.UploadActivity
import com.dicoding.picodiploma.loginwithanimation.data.ui.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListStoryItemAdapter
    private lateinit var progressIndicator: LinearProgressIndicator

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getBooleanExtra("JUST_LOGGED_IN", false)) {
            Toast.makeText(this, R.string.login_successfully, Toast.LENGTH_SHORT).show()
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        progressIndicator = findViewById(R.id.progressIndicator)

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        binding.btnLogout.setOnClickListener {
            mainViewModel.logout()

            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }


        binding.fbUpload.setOnClickListener{
            startActivity(Intent(this, UploadActivity::class.java))
        }

        setupView()
        getData()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun getData() {
        adapter = ListStoryItemAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer =  LoadingStateAdapter{
                adapter.retry()
            }
        )
        mainViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}