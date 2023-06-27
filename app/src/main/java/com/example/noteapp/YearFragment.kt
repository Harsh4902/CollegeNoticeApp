package com.example.noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.adpaters.NoticeAdapter
import com.example.noteapp.adpaters.NoticeClicked
import com.example.noteapp.api.NoticeAPI
import com.example.noteapp.databinding.FragmentMainBinding
import com.example.noteapp.databinding.FragmentYearBinding
import com.example.noteapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class YearFragment : Fragment(), NoticeClicked {

    @Inject
    lateinit var noteAPI: NoticeAPI

    private var _binding: FragmentYearBinding? = null
    private val binding get() = _binding!!
    private val noticeViewModel by viewModels<NoticeViewModel>()

    private lateinit var adapter : NoticeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        noticeViewModel.getAllNotices()

    }

    private fun bindObservers() {
        noticeViewModel.noticeLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    adapter = NoticeAdapter(it.data,this)
                    binding.recyclerView.layoutManager = GridLayoutManager(this.context,2)
                    binding.recyclerView.adapter = adapter
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkResult.Loading -> {
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNoticeClicked(fileName: String) {
        noticeViewModel.downloadNotice(fileName)
    }

}