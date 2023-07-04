package com.jasjeet.marketit.ui.listings

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentListingsListBinding
import com.jasjeet.marketit.model.ListingData
import com.jasjeet.marketit.util.Resource
import com.jasjeet.marketit.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class ListingsFragment : Fragment(R.layout.fragment_listings_list) {
    
    private val viewModel by activityViewModel<MainViewModel>()
    
    private var _binding: FragmentListingsListBinding? = null
    private val binding get() = _binding!!
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListingsListBinding.bind(view)
        
        val listingsAdapter = ListingsAdapter()
        
        binding.apply {
            // Configuring Recycler View
            list.apply {
                adapter = listingsAdapter
                layoutManager = LinearLayoutManager(context)
            }
            
            // Observing Ui state
            observeUiState(listingsAdapter)
            
            // Add Product button
            fab.setOnClickListener {
                navigateForward()
            }
            
            swipeRefresh.apply {
                setOnRefreshListener {
                    viewModel.fetchListings()
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun observeUiState(listingsAdapter: ListingsAdapter) {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.errorText.visibility =
                when (uiState.status) {
                    Resource.Status.LOADING -> {
                        // Loading
                        binding.swipeRefresh.isRefreshing = true
                        GONE
                    }
                    Resource.Status.FAILED -> {
                        // Error occurred while loading data.
                        binding.swipeRefresh.isRefreshing = false
                        VISIBLE
                    }
                    Resource.Status.SUCCESS -> {
                        // Success
                        if (uiState.listings != null){
                            // If the user adds a product him/herself, we need not to make an API call.
                            // We can directly inject the newer item. Usually we can use item ID for
                            // comparison purpose but here, due to restrictions, are going to use list item itself.
                            if (uiState.listings[1] == listingsAdapter.getFirstItem())
                                listingsAdapter.addItem(uiState.listings[0])
                            else
                                listingsAdapter.updateList(uiState.listings)
                        }
                        binding.swipeRefresh.isRefreshing = false
                        GONE
                    }
                }
            
            // Showing error.
            if (uiState.error != null){
                Snackbar.make(
                    requireView(),
                    getString(R.string.error_listings) + " " + uiState.error.toast(),
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.clearError()
            }
        }
    }
    
    private fun navigateForward(){
        findNavController().navigate(R.id.action_listingsFragment_to_addProductFragment)
    }
    
}