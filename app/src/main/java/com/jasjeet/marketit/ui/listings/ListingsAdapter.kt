package com.jasjeet.marketit.ui.listings

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.jasjeet.marketit.R

import com.jasjeet.marketit.placeholder.PlaceholderContent.PlaceholderItem
import com.jasjeet.marketit.databinding.FragmentListingsBinding
import com.jasjeet.marketit.model.ListingData
import com.jasjeet.marketit.model.ListingDataItem
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
        holder.price.text = DecimalFormat("#.#").format(item?.price).toString()
        holder.tax.text = DecimalFormat("#.#").format(item?.tax).toString()
    }
    
    override fun getItemCount(): Int = list?.size ?: 0
    
    inner class ViewHolder(binding: FragmentListingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.tvName
        val type: TextView = binding.tvType
        val price: TextView = binding.tvPrice
        val tax: TextView = binding.tvTax
        val imageView: ShapeableImageView = binding.image
    }
    
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ListingData?) {
        this.list = list
        this.notifyDataSetChanged()
    }
    
    fun addItem(newItem: ListingDataItem) {
        list?.add(0, newItem)
        notifyItemInserted(0)
    }
    
    fun getFirstItem(): ListingDataItem? {
        return list?.get(0)
    }
}