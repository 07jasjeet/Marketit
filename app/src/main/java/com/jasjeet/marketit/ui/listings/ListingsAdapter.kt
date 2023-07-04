package com.jasjeet.marketit.ui.listings

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentListingsBinding
import com.jasjeet.marketit.model.ListingData
import java.text.DecimalFormat

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ListingsAdapter
    : RecyclerView.Adapter<ListingsAdapter.ViewHolder>() {
    
    private var list: ListingData? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentListingsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list?.get(position)
        
        Glide.with(holder.imageView.context)
            .load(item?.image)
            .placeholder(R.drawable.placeholder)
            .into(holder.imageView)
        
        holder.name.text = item?.product_name
        holder.type.text = item?.product_type
        holder.price.text =
            if (item != null)
                "₹" + DecimalFormat("#.#").format(item.price + item.tax).toString()
            else
                "Price unavailable"
        
        // Add to cart
        holder.btnAddToCart.setOnClickListener {
            Toast.makeText(it.context, "Coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun getItemCount(): Int = list?.size ?: 0
    
    inner class ViewHolder(binding: FragmentListingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.tvName
        val type: TextView = binding.tvType
        val price: TextView = binding.tvPrice
        val imageView: ShapeableImageView = binding.image
        val btnAddToCart: ImageView = binding.btnAddToCart
    }
    
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ListingData?) {
        this.list = list
        this.notifyDataSetChanged()
    }
    
}