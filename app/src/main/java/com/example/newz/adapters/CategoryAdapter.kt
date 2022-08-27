package com.example.newz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newz.R
import com.example.newz.databinding.NewsCategoryItemBinding
import com.example.newz.fragments.HomeFragment

class CategoryAdapter(private val listener: HomeFragment) : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(CategoryDiffUtilCallBack()) {

    inner class CategoryViewHolder(private val binding : NewsCategoryItemBinding) : RecyclerView.ViewHolder(binding.root){
        val categoryText = binding.categoryText
    }
    var rowIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NewsCategoryItemBinding.inflate(layoutInflater,parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.categoryText.text = item
        holder.categoryText.setOnClickListener {
            listener.onClick(item)
            rowIndex = position
            notifyDataSetChanged()
        }
        if(rowIndex==position){
            holder.categoryText.setBackgroundResource(R.drawable.selected_category_bg)
            holder.categoryText.setTextColor(holder.categoryText.context.getResources().getColor(R.color.white))
        }
        else
        {
            holder.categoryText.setBackgroundResource(R.drawable.unselected_category_outline)
            holder.categoryText.setTextColor(holder.categoryText.context.getResources().getColor(R.color.purple_500))
        }
    }
}

class CategoryDiffUtilCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

interface CategoryClickListener{
    fun onClick(item : String)
}