package com.jasjeet.marketit.ui.listings

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentListingsListBinding
import com.jasjeet.marketit.util.Resource
import com.jasjeet.marketit.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class ListingsFragment : Fragment(R.layout.fragment_listings_list) {
    
    private val viewModel by activityViewModel<MainViewModel>()
    
    private var _binding: FragmentListingsListBinding? = null
    private val binding get() = _binding!!
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListingsListBinding.bind(view)
        
        val listingsAdapter = ListingsAdapter()
        val searchAdapter = SearchAdapter()
        
        // Configuring Recycler View
        setRecyclerView(listingsAdapter)
        
        // Observing Ui state
        observeUiState(listingsAdapter)
        
        
        // Add Product button
        binding.apply {
            fab.setOnClickListener {
                navigateForward()
            }
            
            searchBarLayout.apply {
                
                rvSearchResult.apply {
                    adapter = searchAdapter
                    layoutManager = LinearLayoutManager(context)
                }
                
                searchView.editText.addTextChangedListener { query ->
                    viewModel.searchForItem(query.toString()){ result ->
                        searchAdapter.updateList(result)
                    }
                }
                
            }
        }
        
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    
    private fun setRecyclerView(listingsAdapter: ListingsAdapter) {
        binding.searchBarLayout.listingsList.apply {
            list.apply {
                adapter = listingsAdapter
                layoutManager = LinearLayoutManager(context)
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.fetchListings()
            }
        }
    }
    
    private fun observeUiState(listingsAdapter: ListingsAdapter) {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            
            binding.apply {
                
                errorText.visibility =
                    when (uiState.status) {
                        Resource.Status.LOADING -> {
                            // Loading
                            searchBarLayout.listingsList.swipeRefresh.isRefreshing = true
                            GONE
                        }
            
                        Resource.Status.FAILED -> {
                            // Error occurred while loading data.
                            searchBarLayout.listingsList.swipeRefresh.isRefreshing = false
                            if (uiState.listings.isNullOrEmpty())
                                VISIBLE
                            else
                                GONE
                        }
            
                        Resource.Status.SUCCESS -> {
                            // Success
                            listingsAdapter.updateList(uiState.listings)
                            searchBarLayout.listingsList.swipeRefresh.isRefreshing = false
                            GONE
                        }
                        
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