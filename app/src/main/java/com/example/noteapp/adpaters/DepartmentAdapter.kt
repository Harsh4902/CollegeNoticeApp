package com.example.noteapp.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.DepartmentItemBinding


class DepartmentAdapter(
    var content : List<String>,
    private val listner : DepartmentClicked
) : RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder>(){

    inner class DepartmentViewHolder(private val binding: DepartmentItemBinding) :
        RecyclerView.ViewHolder(binding.root){

            fun bind(name : String){
                binding.tvDepartment.text = name
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val binding = DepartmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = DepartmentViewHolder(binding)
        binding.tvDepartment.setOnClickListener {
            listner.onDepartmentClick(content[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        holder.bind(content[position])
    }

    override fun getItemCount(): Int {
        return content.size
    }
}

interface DepartmentClicked{
    fun onDepartmentClick(item: String)
}