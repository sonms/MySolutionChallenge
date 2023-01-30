package com.example.mysolutionchallenge.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.databinding.HomeRvitemLayoutBinding


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){
    private lateinit var binding : HomeRvitemLayoutBinding
    var pillItemData = mutableListOf<PillData?>()
    private lateinit var context : Context

    inner class HomeViewHolder(private val binding : HomeRvitemLayoutBinding ) : RecyclerView.ViewHolder(binding.root) {
        private var position : Int? = null
        var pillItem = binding.homePillNameTv
        var pillItemTime = binding.homePillTimeTv
        fun bind(pillData: PillData, position : Int) {
            this.position = position
            pillItem.text = pillData.pillName
            pillItemTime.text = pillData.pillTakeTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        context = parent.context
        binding = HomeRvitemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(pillItemData[position]!!, position)
    }

    override fun getItemCount(): Int {
        return pillItemData.size
    }

}