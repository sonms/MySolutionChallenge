package com.example.mysolutionchallenge.Adapter


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysolutionchallenge.Model.MedicalData
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.databinding.HomeRvitemLayoutBinding
import com.example.mysolutionchallenge.databinding.SearchItemLayoutBinding
import com.example.mysolutionchallenge.databinding.SearchWordLayoutBinding


class SearchItemAdapter : RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder>(){
    private lateinit var binding : SearchItemLayoutBinding
    var searchItemData = mutableListOf<MedicalData?>()
    private lateinit var context : Context

    inner class SearchItemViewHolder(private val binding : SearchItemLayoutBinding ) : RecyclerView.ViewHolder(binding.root) {
        private var position : Int? = null
        var searchItemContent = binding.searchItemTv

        fun bind(searchData: MedicalData, position : Int) {
            this.position = position
            searchItemContent.text = searchData.content

            binding.root.setOnClickListener {
                itemClickListener.onClick(it, layoutPosition, searchItemData[layoutPosition]!!.position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        context = parent.context
        binding = SearchItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(searchItemData[position]!!, position)
    }

    override fun getItemCount(): Int {
        return searchItemData.size
    }

    //데이터 Handle 함수
    /*fun removeData(position: Int) {
        pillItemData.removeAt(position)
        //temp = null
        notifyItemRemoved(position)
    }*/

    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemId: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}