package com.example.kogan_challenge.ui.main
import androidx.lifecycle.viewModelScope

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kogan_challenge.Repository.ProductRepository
import com.example.kogan_challenge.model.Products
import com.example.kogan_challenge.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProductViewModel() : ViewModel() {
    val products: MutableLiveData<Resource<Products>> = MutableLiveData()
    var pageNumber = 1
    var productsResponse : Products? = null

    init {
        getProducts(pageNumber)
    }

    fun getProducts(pageNumber : Int) = viewModelScope.launch {

        products.postValue(Resource.Loading())

        try {

                val response = ProductRepository.getProduct(pageNumber)
                products.postValue(handleProductResponse(response))


        } catch (t: Throwable){
            when(t) {
                is IOException ->   products.postValue(Resource.Error("Network Failure"))
                else -> products.postValue(Resource.Error("Network Failure"))
            }
        }

    }

    fun handleProductResponse(response: Response<Products>) : Resource<Products>{
        if (response.isSuccessful)
        {
            response.body()?.let { result ->
                pageNumber++
                println("DEBUG page number${pageNumber}")
                if(productsResponse == null)
                {
                    productsResponse = result
                }else
                {
                    val oldProducts = productsResponse?.objects
                    val newProducts = result?.objects
                    oldProducts?.addAll(newProducts)
                }
                return Resource.Success(productsResponse?:result)
            }
        }
        return Resource.Error(response.message())
    }

}





