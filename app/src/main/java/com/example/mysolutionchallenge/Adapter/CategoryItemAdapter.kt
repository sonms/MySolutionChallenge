package com.example.mysolutionchallenge.Adapter


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysolutionchallenge.Model.CategoryData
import com.example.mysolutionchallenge.Model.MedicalData
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.databinding.CategoryRvitemLayoutBinding
import com.example.mysolutionchallenge.databinding.HomeRvitemLayoutBinding
import com.example.mysolutionchallenge.databinding.SearchItemLayoutBinding
import com.example.mysolutionchallenge.databinding.SearchWordLayoutBinding


class CategoryItemAdapter : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemRVViewHolder>(){
    private lateinit var categoryItemRVBinding: HomeRvitemLayoutBinding
    var categoryItemData = mutableListOf<PillData?>()
    private lateinit var context : Context


    inner class CategoryItemRVViewHolder(var categoryRvitemLayoutBinding : HomeRvitemLayoutBinding ) : RecyclerView.ViewHolder(categoryRvitemLayoutBinding.root) {
        private var position : Int? = null
        var pillItem = categoryItemRVBinding.homePillNameTv
        var pillItemTime = categoryItemRVBinding.homePillTimeTv

        fun bind(pillData: PillData, position : Int) {
            this.position = position
            pillItem.text = pillData.pillName
            pillItemTime.text = pillData.pillTakeTime

            categoryRvitemLayoutBinding.root.setOnClickListener {
                itemClickListener!!.onClick(it,layoutPosition,categoryItemData[layoutPosition]!!.position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemAdapter.CategoryItemRVViewHolder {
        var inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        categoryItemRVBinding = HomeRvitemLayoutBinding.inflate(inflater, parent, false)
        return CategoryItemRVViewHolder(categoryItemRVBinding)
    }

    override fun onBindViewHolder(holder: CategoryItemRVViewHolder, position: Int) {
        holder.bind(categoryItemData[position]!!, position)
        val content : PillData = categoryItemData[position]!!
        categoryItemRVBinding.homeRemoveIv.setOnClickListener {
            val builder : AlertDialog.Builder = AlertDialog.Builder(context)
            val ad : AlertDialog = builder.create()
            var deleteData = categoryItemData[holder.adapterPosition]!!.pillName
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
        return categoryItemData.size
    }

    //데이터 Handle 함수
    /*fun removeData(position: Int) {
        pillItemData.removeAt(position)
        //temp = null
        notifyItemRemoved(position)
    }*/

    fun removeData(position: Int) {
        categoryItemData.removeAt(position)
        //temp = null
        notifyItemRemoved(position)
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemId: Int)
    }

    private var itemClickListener: CategoryAdapter.ItemClickListener? = null

    fun setItemClickListener(itemClickListener: CategoryAdapter.ItemClickListener) {
        this.itemClickListener = itemClickListener
    }



}