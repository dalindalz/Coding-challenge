package com.example.kogan_challenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kogan_challenge.R
import com.example.kogan_challenge.model.Object
import kotlinx.android.synthetic.main.item_product_view.view.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private val diffCallBack = object :DiffUtil.ItemCallback<Object>(){

        override fun areItemsTheSame(oldItem: Object, newItem: Object): Boolean {
            return oldItem.title.toString() == newItem.title.toString()
        }

        override fun areContentsTheSame(oldItem: Object, newItem: Object): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product_view,
                parent,
                false

            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.itemView.apply {
            tvTitle.text = product.title
            if (product.cubicWeight == 0.00){
                layout_cubic.visibility = View.GONE
            }else
            {
                layout_cubic.visibility = View.VISIBLE

            }
            tvCC.text = product.cubicWeight.toString()
            tvCategory.text = product.category
            val dimension = product.size.length.toString() + " L * " + product.size.width.toString() + " W * " + product.size.height.toString() + " H"
            tvDimension.text = dimension
        }
    }

}