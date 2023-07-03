package com.jasjeet.marketit.ui.addproduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.jasjeet.marketit.viewmodel.AddProductViewModel
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentAddProductBinding
import com.jasjeet.marketit.model.ListingDataItem
import com.jasjeet.marketit.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddProductFragment : Fragment(R.layout.fragment_add_product) {
    
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel by viewModel<AddProductViewModel>()
    private val mainViewModel by activityViewModel<MainViewModel>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddProductBinding.bind(view)
        
        binding.apply {
            btnAdd.setOnClickListener {
                viewModel.addProduct(price = "10", name = "Bux", type = "Soap", tax = "13")
                mainViewModel.alertProductAdded(ListingDataItem(price = 10.0, product_name = "Lux", product_type = "Soap", tax = 13.0))
            }
            
            // Observing Ui State
            observeUiState()
            
        }
    }
    
    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.error != null){
                Toast.makeText(activity, getString(R.string.error_add_product) + " " + it.error.toast(), Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}