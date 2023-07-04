package com.jasjeet.marketit.ui.addproduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jasjeet.marketit.viewmodel.AddProductViewModel
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentAddProductBinding
import com.jasjeet.marketit.model.ListingDataItem
import com.jasjeet.marketit.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                
                val name = productName.text
                val type = productType.text
                val price = productPrice.text
                val tax = productTax.text
                
                // Checking if any field is empty or not.
                if (
                    name.isNullOrEmpty() ||
                    type.isNullOrEmpty() ||
                    price.isNullOrEmpty() ||
                    tax.isNullOrEmpty()
                ){
                    setErrorText(R.string.error_fields_empty)
                    return@setOnClickListener
                }
                
                // Checking if price and do not have letters in them.
                val (priceDouble, taxDouble) = try {
                    Pair(price.toString().toDouble(), tax.toString().toDouble())
                } catch (e: NumberFormatException){
                    setErrorText(R.string.error_fields_invalid)
                    return@setOnClickListener
                }
                
                viewModel.addProduct(
                    name = name.toString(),
                    type = type.toString(),
                    price = price.toString(),
                    tax = tax.toString()
                )
                
                mainViewModel.alertProductAdded(
                    ListingDataItem(
                        product_name = name.toString(),
                        product_type = type.toString(),
                        price = priceDouble,
                        tax = taxDouble
                    )
                )
                
                findNavController().popBackStack()
            }
            
            // Observing Ui State
            observeUiState()
            
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.error != null){
                Toast.makeText(activity, getString(R.string.error_add_product) + " " + it.error.toast(), Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
    
    private fun setErrorText(@StringRes id: Int){
        lifecycleScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main){
                binding.errorText.text = getString(id)
            }
            // Delay for erasing the error.
            delay(3000)
            
            withContext(Dispatchers.Main) { binding.errorText.text = null }
            
        }
    }
    
}