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


class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryItemViewHolder>(){
    private lateinit var categoryRvitemLayoutBinding: CategoryRvitemLayoutBinding
    var categoryItemData = mutableListOf<CategoryData?>()
    private lateinit var context : Context


    inner class CategoryItemViewHolder(var categoryRvitemLayoutBinding : CategoryRvitemLayoutBinding ) : RecyclerView.ViewHolder(categoryRvitemLayoutBinding.root) {
        private var position : Int? = null
        var categoryItemContent = categoryRvitemLayoutBinding.categoryNameTv

        fun bind(categoryData: CategoryData, position : Int) {
            this.position = position
            categoryItemContent.text = categoryData.categoryName

            categoryRvitemLayoutBinding.root.setOnClickListener {
                itemClickListener!!.onClick(it,layoutPosition,categoryItemData[layoutPosition]!!.position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.CategoryItemViewHolder {
        var inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        categoryRvitemLayoutBinding = CategoryRvitemLayoutBinding.inflate(inflater, parent, false)
        return CategoryItemViewHolder(categoryRvitemLayoutBinding)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bind(categoryItemData[position]!!, position)
        val content : CategoryData = categoryItemData[position]!!
        holder.categoryItemContent.text = content!!.categoryName
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

    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemId: Int)
    }

    private var itemClickListener: CategoryAdapter.ItemClickListener? = null

    fun setItemClickListener(itemClickListener: CategoryAdapter.ItemClickListener) {
        this.itemClickListener = itemClickListener
    }



}