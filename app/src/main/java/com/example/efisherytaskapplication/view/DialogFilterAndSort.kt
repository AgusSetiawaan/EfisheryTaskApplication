package com.example.efisherytaskapplication.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.efisherytaskapplication.R
import com.example.efisherytaskapplication.data.FilterAndSortModel
import com.example.efisherytaskapplication.data.SortType
import com.example.efisherytaskapplication.databinding.SortAndFilterScreenBinding
import com.example.efisherytaskapplication.response.OptionArea
import com.example.efisherytaskapplication.response.OptionSize
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogFilterAndSort: BottomSheetDialogFragment() {

    private lateinit var binding: SortAndFilterScreenBinding
    private var optionAreaList = listOf<OptionArea>()
    private var optionSizeList = listOf<OptionSize>()

    private var onClickSubmitButton: (FilterAndSortModel) -> Unit = {}

    private var province = ""
    private var city = ""
    private var sizeStart = -1
    private var sizeEnd = -1
    private var priceStart = -1
    private var priceEnd = -1
    private var sortType = SortType.DEFAULT

    companion object{

        const val TAG = "DIALOG_FILTER_SORT"

        fun newInstance(optionAreaList: List<OptionArea>, optionSizeList: List<OptionSize>, onClickSubmitButton: (FilterAndSortModel) -> Unit) = DialogFilterAndSort().apply {
            this.optionAreaList = optionAreaList
            this.optionSizeList = optionSizeList
            this.onClickSubmitButton = onClickSubmitButton
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        if(bottomSheetDialog is BottomSheetDialog){
            bottomSheetDialog.behavior.skipCollapsed = true
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SortAndFilterScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOptionArea(optionAreaList)
        setOptionSize(optionSizeList)
        setSort()

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.submitButton.setOnClickListener {
            if(binding.priceStartEditText.text.toString().isNotBlank()) priceStart = binding.priceStartEditText.text.toString().toInt()
            if(binding.priceEndEditText.text.toString().isNotBlank()) priceStart = binding.priceEndEditText.text.toString().toInt()
            onClickSubmitButton(
                FilterAndSortModel(
                    sortType,
                    province,
                    city,
                    sizeStart,
                    sizeEnd,
                    priceStart,
                    priceEnd
                )
            )
            dismiss()
        }

    }

    private fun setSort(){
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, enumValues<SortType>().map { it.desc })
        binding.sortEditText.setAdapter(arrayAdapter)
        (binding.sortEditText).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                sortType = enumValues<SortType>()[position]
            }
    }

    private fun setOptionArea(list: List<OptionArea>){
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list.map { it.province }.distinct())
        binding.provinceEditText.setAdapter(arrayAdapter)

        (binding.provinceEditText).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedValue = arrayAdapter.getItem(position)
                province = selectedValue?:""
                val arrayAdapterCity = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    list.filter { it.province == selectedValue }.map { it.city })
                binding.cityEditText.setAdapter(arrayAdapterCity)
            }

        (binding.cityEditText).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedValue = arrayAdapter.getItem(position)
                city = selectedValue?:""
            }
    }

    private fun setOptionSize(list: List<OptionSize>){
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list.map { it.size })
        binding.sizeStartEditText.setAdapter(arrayAdapter)

        (binding.sizeStartEditText).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedValue = arrayAdapter.getItem(position)
                sizeStart = selectedValue?.toInt()?:-1
                val arrayAdapterCity = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    list.filter { it.size.toInt() >= (selectedValue?.toInt() ?: 0) }.map { it.size })
                binding.sizeEndEditText.setAdapter(arrayAdapterCity)
            }

        (binding.sizeEndEditText).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedValue = arrayAdapter.getItem(position)
                sizeEnd = selectedValue?.toInt()?:-1
            }
    }
}