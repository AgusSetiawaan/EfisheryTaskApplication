package com.example.efisherytaskapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.efisherytaskapplication.CommodityInputScreenViewModel
import com.example.efisherytaskapplication.R
import com.example.efisherytaskapplication.Result
import com.example.efisherytaskapplication.databinding.CommodityInputScreenBinding
import com.example.efisherytaskapplication.response.CommodityRequest
import com.example.efisherytaskapplication.response.OptionArea
import com.example.efisherytaskapplication.response.OptionSize
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommodityInputScreenView: Fragment() {

    private lateinit var binding: CommodityInputScreenBinding
    private val viewModel: CommodityInputScreenViewModel by viewModels()

    private var province = ""
    private var city = ""
    private var size = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommodityInputScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()

        viewModel.getAllList()

        binding.submitButton.setOnClickListener {
            if(binding.commodityEditText.text.toString().isBlank() ||
                province.isBlank() || city.isBlank() || size.isBlank() ||
                binding.priceEditText.text.toString().isBlank()){
                Toast.makeText(requireContext(), "Mohon Lengkapi Kolom yang Kosong", Toast.LENGTH_SHORT).show()
            }
            else{
                viewModel.postData(CommodityRequest(
                    binding.commodityEditText.text.toString(),
                    province,
                    city,
                    size,
                    binding.priceEditText.text.toString()))
            }

        }

        binding
    }

    private fun observeViewModel(){
        viewModel.optionAreaListLiveData.observe(viewLifecycleOwner){
            setOptionArea(it)
        }
        viewModel.optionSizeListLiveData.observe(viewLifecycleOwner){
            setOptionSize(it)
        }
        viewModel.postDataLiveData.observe(viewLifecycleOwner){
            when(it){
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), "Sukses Mengirim Data", Toast.LENGTH_SHORT).show()
                }
                is Result.Error ->{
                    showLoading(false)
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    private fun setOptionArea(list: List<OptionArea>){
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list.map { it.province }.distinct())
        binding.provinceEditText.setAdapter(arrayAdapter)

        (binding.provinceEditText).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedValue = arrayAdapter.getItem(position)
                province = selectedValue?:""
                val arrayAdapterCity = ArrayAdapter(requireContext(), R.layout.dropdown_item, list.filter { it.province == selectedValue }.map { it.city })
                binding.cityEditText.setAdapter(arrayAdapterCity)
            }

        (binding.cityEditText).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedValue = arrayAdapter.getItem(position)
                city = selectedValue?:""
            }
    }

    private fun setOptionSize(list: List<OptionSize>){
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list.map { it.size })
        binding.sizeEditText.setAdapter(arrayAdapter)

        (binding.sizeEditText).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val selectedValue = arrayAdapter.getItem(position)
                size = selectedValue?:""
            }
    }
}