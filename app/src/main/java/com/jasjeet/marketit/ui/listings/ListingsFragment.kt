package com.jasjeet.marketit.ui.listings

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentListingsListBinding
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
            
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun observeUiState(listingsAdapter: ListingsAdapter) {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.progressIndicator.visibility =
                when (uiState.status) {
                    Resource.Status.LOADING -> {
                        // Loading
                        removeErrorText()
                        VISIBLE
                    }
                    Resource.Status.FAILED -> {
                        // Error occurred while loading data.
                        setErrorTextVisible()
                        GONE
                    }
                    Resource.Status.SUCCESS -> {
                        // Success
                        removeErrorText()
                        listingsAdapter.updateList(uiState.listings)
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
    
    private fun setErrorTextVisible() {
        if (!binding.errorText.isVisible)
            binding.errorText.visibility = VISIBLE
    }
    
    private fun removeErrorText() {
        if (binding.errorText.isVisible)
            binding.errorText.visibility = GONE
    }
    
}