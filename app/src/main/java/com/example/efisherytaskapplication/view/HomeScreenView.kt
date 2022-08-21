package com.example.efisherytaskapplication.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.efisherytaskapplication.CommodityListAdapter
import com.example.efisherytaskapplication.HomeScreenViewModel
import com.example.efisherytaskapplication.R
import com.example.efisherytaskapplication.Result
import com.example.efisherytaskapplication.data.CommodityData
import com.example.efisherytaskapplication.data.FilterAndSortModel
import com.example.efisherytaskapplication.databinding.HomeScreenViewBinding
import com.example.efisherytaskapplication.response.OptionArea
import com.example.efisherytaskapplication.response.OptionSize
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenView: Fragment() {

    private lateinit var binding: HomeScreenViewBinding
    private val viewModel: HomeScreenViewModel by viewModels()

    private var optionAreaList = listOf<OptionArea>()
    private var optionSizeList = listOf<OptionSize>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeScreenViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.queryHint = getString(R.string.search_hint)
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUsers(query)
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })

        binding.btnAddCommodity.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenView_to_commodityInputScreenView)
        }

        binding.btnSort.setOnClickListener {
            DialogFilterAndSort.newInstance(optionAreaList, optionSizeList, ::onClickSubmitButton).show(childFragmentManager, DialogFilterAndSort.TAG)
        }

        observeViewModel()

        viewModel.getAllList()
    }

    private fun searchUsers(query: String){
        viewModel.getSearchCommodity(query).observe(viewLifecycleOwner){
            initList(it)
        }
    }

    private fun observeViewModel(){
        viewModel.commodityListLiveData.observe(viewLifecycleOwner) {
            when(it){
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    initList(it.data)
                }
                is Result.Error ->{
                    showLoading(false)
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    viewModel.getAllListFromDatabase().observe(viewLifecycleOwner){data ->
                        initList(data)
                    }
                }
            }

        }
        viewModel.optionAreaListLiveData.observe(viewLifecycleOwner){
            optionAreaList = it
        }
        viewModel.optionSizeListLiveData.observe(viewLifecycleOwner){
            optionSizeList = it
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        binding.btnSort.visibility = if(isLoading) View.GONE else View.VISIBLE
    }

    private fun initList(list: List<CommodityData>){
        val adapter = CommodityListAdapter()

        adapter.submitList(list)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCommodity.layoutManager = layoutManager
        binding.rvCommodity.adapter = adapter
    }

    private fun onClickSubmitButton(data: FilterAndSortModel){
        viewModel.getFilteredCommodity(data).observe(viewLifecycleOwner){
            initList(it)
        }
    }

}