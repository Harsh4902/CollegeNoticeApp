package com.example.noteapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.noteapp.databinding.FragmentRegisterBinding
import com.example.noteapp.model.UserRegister
import com.example.noteapp.model.UserRequest
import com.example.noteapp.utils.Constants
import com.example.noteapp.utils.Constants.TAG
import com.example.noteapp.utils.NetworkResult
import com.example.noteapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        Log.d(TAG,tokenManager.getToken().toString())
        if(tokenManager.getToken() != null){
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            //findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }

        binding.imageView2.setOnClickListener {
            val result = validateUserInput()
            if(!result.first){
                binding.textViewError.text = result.second
            }
            else {
                authViewModel.registerUser(
                    getUserRequest()
                )
            }
        }
        bindObserver()

    }

    private fun getUserRequest() : UserRegister{
        val email = binding.editTextTextPersonEmail.text.toString().trim()
        val list = binding.editTextTextPersonName.text.toString().split("")
        val password = binding.editTextTextPersonName3.text.toString().trim()

        return UserRegister(email,list.get(0),list.get(1),password)
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            binding.textViewError.text = null
            when(it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.textViewError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val email = binding.editTextTextPersonEmail.text.toString().trim()
        val list = binding.editTextTextPersonName.text.toString().split("")
        val password = binding.editTextTextPersonName3.text.toString().trim()

        return authViewModel.validateCredential(binding.editTextTextPersonName.text.toString(), email, password)
    }

}