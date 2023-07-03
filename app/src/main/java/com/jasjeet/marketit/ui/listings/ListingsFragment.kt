package com.jasjeet.marketit.ui.listings

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentListingsListBinding
import com.jasjeet.marketit.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class ListingsFragment : Fragment(R.layout.fragment_listings_list) {
    
    private var columnCount = 1
    private val viewModel by activityViewModel<MainViewModel>()
    
    private var _binding: FragmentListingsListBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListingsListBinding.bind(view)
        
        val listingsAdapter = ListingsAdapter()
        
        binding.apply {
            // Configuring Recycler View
            list.apply {
                adapter = listingsAdapter
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
            }
            
            // Observing Ui state
            observeUiState(listingsAdapter)
            
            // Add Product button
            fab.setOnClickListener {
                navigateForward()
            }
            
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun observeUiState(listingsAdapter: ListingsAdapter) {
        viewModel.uiState.observe(viewLifecycleOwner) {
            listingsAdapter.updateList(it.listings)
            if (it.error != null){
                Toast.makeText(context, getString(R.string.error_listings) + " " + it.error.toast(), Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
    
    private fun navigateForward(){
        findNavController().navigate(R.id.action_listingsFragment_to_addProductFragment)
    }
    
    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
    }
}