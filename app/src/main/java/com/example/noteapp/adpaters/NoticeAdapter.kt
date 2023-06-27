package com.example.noteapp.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.DepartmentItemBinding
import com.example.noteapp.databinding.FragmentYearBinding
import com.example.noteapp.databinding.PdfItemBinding
import com.example.noteapp.model.NoticeResponse

class NoticeAdapter (
    val content : List<NoticeResponse>?,
    val listner : NoticeClicked
    ) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>(){

    inner class NoticeViewHolder(private val binding:PdfItemBinding) :
        RecyclerView.ViewHolder(binding.root){

            fun bind(notice : NoticeResponse){
                binding.tvFileName.text = notice.fileName
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = PdfItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = NoticeViewHolder(binding)
        binding.root.setOnClickListener {
            listner.onNoticeClicked(binding.tvFileName.text.toString())
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(content!![position])
    }

    override fun getItemCount(): Int {
        return content!!.size
    }


}

interface NoticeClicked{
    fun onNoticeClicked(fileName : String)
}