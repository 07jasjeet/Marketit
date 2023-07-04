package com.jasjeet.marketit.ui.listings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentListingsBinding
import com.jasjeet.marketit.databinding.SearchResultBinding
import com.jasjeet.marketit.model.ListingData
import java.text.DecimalFormat

class SearchAdapter
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    
    private var list: ListingData? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchResultBinding.inflate(
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
        holder.price.text =
            if (item != null)
                "₹" + DecimalFormat("#.#").format(item.price + item.tax).toString()
            else
                "Unavailable"
    }
    
    override fun getItemCount(): Int = list?.size ?: 0
    
    inner class ViewHolder(binding: SearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.tvName
        val price: TextView = binding.tvPrice
        val imageView: ShapeableImageView = binding.image
    }
    
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ListingData?) {
        this.list = list
        this.notifyDataSetChanged()
    }
    
}