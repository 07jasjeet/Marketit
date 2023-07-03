package com.jasjeet.marketit.ui.listings

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.jasjeet.marketit.placeholder.PlaceholderContent.PlaceholderItem
import com.jasjeet.marketit.databinding.FragmentListingsBinding
import com.jasjeet.marketit.model.ListingData
import com.jasjeet.marketit.model.ListingDataItem

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
        
        holder.idView.text = position.toString()
        holder.contentView.text = item?.product_name
        
    }
    
    override fun getItemCount(): Int = list?.size ?: 0
    
    inner class ViewHolder(binding: FragmentListingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        
        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
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
    
}