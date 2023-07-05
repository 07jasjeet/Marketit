package com.jasjeet.marketit.ui.addproduct

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jasjeet.marketit.R
import com.jasjeet.marketit.databinding.FragmentAddProductBinding
import com.jasjeet.marketit.util.Resource
import com.jasjeet.marketit.viewmodel.AddProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException

class AddProductFragment : Fragment(R.layout.fragment_add_product) {
    
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel by viewModel<AddProductViewModel>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddProductBinding.bind(view)
        
        binding.apply {
    
            // Observing Ui State
            observeUiState()
            
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            
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
                
                viewModel.addProduct(
                    name = name.toString(),
                    type = type.toString(),
                    price = price.toString(),
                    tax = tax.toString(),
                )
                
            }
            
            btnSelectImage.setOnClickListener {
                if (tvSelectImage.text == getString(R.string.select_an_image))
                    launchGallery()
                else
                    tvSelectImage.text = getString(R.string.select_an_image)
            }
            
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            
            if (uiState.status == Resource.Status.SUCCESS){
                Toast.makeText(activity, getString(R.string.success_add_product), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            
            if (uiState.error != null){
                Toast.makeText(activity, getString(R.string.error_add_product) + " " + uiState.error.toast(), Toast.LENGTH_LONG).show()
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
    
    private fun launchGallery() {
        // Permissions required.
        val permission : String =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE
        
        // Different scenarios with permissions
        when{
            ContextCompat.checkSelfPermission(requireContext(),permission)
                    == PackageManager.PERMISSION_GRANTED
            -> {
                val pickIntent = Intent(Intent.ACTION_PICK)
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                
                // URI is path of the image inside external storage
                openGalleryLauncher.launch(pickIntent)
                
            }
            shouldShowRequestPermissionRationale(permission) -> showRationaleDialog()
            else -> requestPermission.launch( arrayOf(permission) )
        }
    }
    
    
    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult( ActivityResultContracts.StartActivityForResult() ){
                result ->
            if( result.resultCode == RESULT_OK && result.data != null){
                // Mutating state of Ui and storing the Uri.
                binding.tvSelectImage.text = getString(R.string.selected)
                
                val file : File? = try {
                    uriToImageFile(result.data?.data!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                
                viewModel.setImageFile(file)
            }
        }
    
    private fun uriToImageFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val filePath = cursor.getString(columnIndex)
                cursor.close()
                return File(filePath)
            }
            cursor.close()
        }
        return null
    }
    
    // Permissions
    private val requestPermission : ActivityResultLauncher<Array<String>> =
        registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { isGranted ->
                if (!isGranted.value) {
                    Snackbar.make(requireView(), "Access Denied", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    
    private fun showRationaleDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        with(dialog){
            setTitle("No Storage Access Granted")
            setMessage("Cannot access gallery due to permission restrictions from system.")
            setPositiveButton("Ask Again"){ _, _ ->
                requestPermission.launch(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            }
            setNegativeButton("Cancel"){dialog, _->
                dialog.dismiss()
            }
            setNeutralButton("Help"){_,_ ->
                val helpDialog = AlertDialog.Builder(requireContext())
                with(helpDialog){
                    setTitle("Grant Access Manually")
                    setMessage("To grant access manually, Hold app icon > Click \"App info\" > Permissions")
                    setPositiveButton("OK"){helpDialog , _ ->
                        helpDialog.dismiss()
                    }
                    show()
                }
            }
            show()
        }
    }
}