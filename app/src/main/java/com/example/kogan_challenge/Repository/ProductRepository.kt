package com.example.kogan_challenge.Repository

import com.example.kogan_challenge.api.RetrofitInstance

object ProductRepository {
    suspend fun getProduct(pageNumber : Int) =
        RetrofitInstance.api.getProducts(pageNumber)
}