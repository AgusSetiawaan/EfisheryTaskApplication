package com.example.efisherytaskapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.efisherytaskapplication.data.CommodityData
import com.example.efisherytaskapplication.databinding.ItemCommodityBinding
import com.example.efisherytaskapplication.databinding.ItemCommodityTableBinding

class CommodityListAdapter: ListAdapter<CommodityData, CommodityListAdapter.CommodityListViewHolder>(
    DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommodityListViewHolder {
        val binding = ItemCommodityTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommodityListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommodityListViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            (holder).bind(data)
        }
    }

    class CommodityListViewHolder(private val binding: ItemCommodityTableBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: CommodityData){
            binding.tvCommodity.text = item.commodity
            binding.tvPrice.text = item.price.toString()
            binding.tvSize.text = item.size.toString()
            binding.tvProvince.text = item.province
            binding.tvCity.text = item.city
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CommodityData>() {
            override fun areItemsTheSame(oldItem: CommodityData, newItem: CommodityData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CommodityData, newItem: CommodityData): Boolean {
                return oldItem.uuid == newItem.uuid
            }
        }
    }

}

