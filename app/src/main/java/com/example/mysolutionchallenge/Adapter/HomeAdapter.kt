package com.example.mysolutionchallenge.Adapter


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
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

            binding.root.setOnClickListener {
                itemClickListener.onClick(it, layoutPosition, pillItemData[layoutPosition]!!.position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        context = parent.context
        binding = HomeRvitemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(pillItemData[position]!!, position)
        binding.homeRemoveIv.setOnClickListener {
            val builder : AlertDialog.Builder = AlertDialog.Builder(context)
            val ad : AlertDialog = builder.create()
            var deleteData = pillItemData[holder.adapterPosition]!!.pillName
            builder.setTitle(deleteData)
            builder.setMessage("정말로 삭제하시겠습니까?")

            builder.setNegativeButton("예",
                DialogInterface.OnClickListener { dialog, which ->
                    ad.dismiss()
                    //temp = listData[holder.adapterPosition]!!
                    //extraditeData()
                    //testData.add(temp)
                    //deleteServerData = tempServerData[holder.adapterPosition]!!.api_id
                    removeData(holder.adapterPosition)
                    //removeServerData(deleteServerData!!)
                    //println(deleteServerData)
                })

            builder.setPositiveButton("아니오",
                DialogInterface.OnClickListener { dialog, which ->
                    ad.dismiss()
                })
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return pillItemData.size
    }

    //데이터 Handle 함수
    fun removeData(position: Int) {
        pillItemData.removeAt(position)
        //temp = null
        notifyItemRemoved(position)
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemId: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}