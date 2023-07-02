package com.jasjeet.marketit.ui.addproduct

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jasjeet.marketit.viewmodel.AddProductViewModel
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentAddProductBinding
import com.jasjeet.marketit.viewmodel.ListingsViewModel

class AddProductFragment : Fragment(R.layout.fragment_add_product) {
    
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AddProductViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddProductBinding.bind(view)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}