package com.jasjeet.marketit.ui.listings

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentListingsListBinding
import com.jasjeet.marketit.placeholder.PlaceholderContent
import com.jasjeet.marketit.viewmodel.AddProductViewModel
import com.jasjeet.marketit.viewmodel.ListingsViewModel


class ListingsFragment : Fragment(R.layout.fragment_listings_list) {
    
    private var columnCount = 1
    private val viewModel: ListingsViewModel by viewModels()
    
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
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_listings_list, container, false)
        
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyListingsRecyclerViewAdapter(PlaceholderContent.ITEMS){
                    // TODO: remove this
                    findNavController().navigate(R.id.addProductFragment)
                }
            }
        }
        return view
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        
        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ListingsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}