package com.example.kogan_challenge.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.kogan_challenge.R
import com.example.kogan_challenge.Repository.ProductRepository

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

    }
}
