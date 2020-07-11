package com.example.kogan_challenge.ui.main
import androidx.lifecycle.viewModelScope

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kogan_challenge.Repository.ProductRepository
import com.example.kogan_challenge.model.Products
import com.example.kogan_challenge.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductViewModel() : ViewModel() {
    val products: MutableLiveData<Resource<Products>> = MutableLiveData()
    var pageNumber = 1
    var productsResponse : Products? = null

    init {
        getProducts(pageNumber)
    }

    fun getProducts(pageNumber : Int) = viewModelScope.launch {

        products.postValue(Resource.Loading())
        val response = ProductRepository.getProduct(pageNumber)
        products.postValue(handleProductResponse(response))

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





