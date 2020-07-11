package com.example.kogan_challenge.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kogan_challenge.R
import com.example.kogan_challenge.adapter.ProductAdapter
import com.example.kogan_challenge.model.Products
import com.example.kogan_challenge.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.kogan_challenge.util.Resource
import kotlinx.android.synthetic.main.main_fragment.*
import java.math.RoundingMode
import java.text.DecimalFormat

class MainFragment : Fragment(R.layout.main_fragment){
    lateinit var viewModel: ProductViewModel
    lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this).get(ProductViewModel::class.java)
        }?: throw Exception("Invalid Activity")


        setupRecyclerView()

        viewModel.products.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->{
                    response.data?.let { productResponse ->
                        calculate(productResponse)
                        val totalPages = 6
                        isLastPage = viewModel.pageNumber == totalPages
                        if (isLastPage)
                        {
                            //rvProductList.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("MainFragment", "An error occured: $message")
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })

    }


    private fun calculate(productResponse: Products)
    {
        for (item in productResponse.objects)
        {
            if (item.category == "Air Conditioners" )
            {
                item.cubicWeight = ((item.size.height/100) * (item.size.width/100) * (item.size.length/100)) * 250
                item.cubicWeight = roundOffDecimal(item.cubicWeight)
            }

        }
        productAdapter.differ.submitList(productResponse.objects.toList())
        hideProgressBar()

    }

    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }




    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                println()
                viewModel.getProducts(viewModel.pageNumber)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView()
    {
        productAdapter = ProductAdapter()


        rvProductList.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@MainFragment.scrollListener)
        }
    }
}
