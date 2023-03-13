package com.example.mysolutionchallenge.Navigation

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysolutionchallenge.Adapter.CategoryAdapter
import com.example.mysolutionchallenge.Model.CategoryData
import com.example.mysolutionchallenge.R
import com.example.mysolutionchallenge.databinding.FragmentCategoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var categoryBinding: FragmentCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private var manager : LinearLayoutManager = LinearLayoutManager(activity)
    private var categoryNameData : MutableList<CategoryData?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryBinding = FragmentCategoryBinding.inflate(inflater, container, false)

        initRecyclerView()

        categoryBinding.addcategoryIV.setOnClickListener {
            val et = EditText(activity)
            et.gravity = Gravity.CENTER
            val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
            val ad : AlertDialog = builder.create()
            builder.setTitle("카테고리 이름")
            builder.setView(et)
            builder.setPositiveButton("확인",DialogInterface.OnClickListener { dialogInterface, i ->
                categoryNameData.add(CategoryData(0, et.text.toString()))
            })
            builder.setNegativeButton("취소",DialogInterface.OnClickListener { dialogInterface, i ->
                ad.dismiss()
            })
            builder.show()

            categoryAdapter!!.notifyDataSetChanged()
        }

        return categoryBinding.root
    }

    private fun initRecyclerView() {
        categoryAdapter = CategoryAdapter()
        categoryAdapter!!.categoryItemData = categoryNameData
        categoryBinding.categoryRV.adapter = categoryAdapter
        //레이아웃 뒤집기 안씀
        //manager.reverseLayout = true
        //manager.stackFromEnd = true
        categoryBinding.categoryRV.setHasFixedSize(true)
        categoryBinding.categoryRV.layoutManager = manager
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}