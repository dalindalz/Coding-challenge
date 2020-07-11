package com.example.kogan_challenge.api

import com.example.kogan_challenge.model.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ProductAPI {

    @GET("/api/products/{page}")
    suspend fun getProducts(
        @Path("page") pageNumber: Int
    ): Response<Products>

}