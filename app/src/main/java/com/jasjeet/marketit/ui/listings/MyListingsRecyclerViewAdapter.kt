package com.jasjeet.marketit.ui.listings

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController

import com.jasjeet.marketit.placeholder.PlaceholderContent.PlaceholderItem
import com.jasjeet.marketit.databinding.FragmentListingsBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyListingsRecyclerViewAdapter(
    private val values: List<PlaceholderItem>,
    private val onClick: () -> Unit
) : RecyclerView.Adapter<MyListingsRecyclerViewAdapter.ViewHolder>() {
    
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
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
        holder.contentView.setOnClickListener {
            onClick()
        }
    }
    
    override fun getItemCount(): Int = values.size
    
    inner class ViewHolder(binding: FragmentListingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        
        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
    
}