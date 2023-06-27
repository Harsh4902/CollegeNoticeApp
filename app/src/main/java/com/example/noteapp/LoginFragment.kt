package com.example.noteapp

import android.net.Network
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
import com.example.noteapp.databinding.FragmentLoginBinding
import com.example.noteapp.databinding.FragmentRegisterBinding
import com.example.noteapp.model.UserRequest
import com.example.noteapp.utils.Constants
import com.example.noteapp.utils.NetworkResult
import com.example.noteapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageView2.setOnClickListener{
            val result = validateUserInput()
            if(!result.first){
                binding.textViewError.text = result.second
            }
            else {
                authViewModel.loginUser(
                    getUserRequest()
                )
            }
        }

        bindingObserver()
    }

    private fun getUserRequest(): UserRequest{
        val email = binding.editTextTextPersonEmail.text.toString().trim()
        val password = binding.editTextTextPersonName3.text.toString().trim()

        return UserRequest(email, password)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val email = binding.editTextTextPersonEmail.text.toString().trim()
        val password = binding.editTextTextPersonName3.text.toString().trim()

        return authViewModel.validateCredentialForLogin(email, password)
    }

    private fun bindingObserver(){
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.textViewError.text = null
            binding.progressBar2.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    Log.d(Constants.TAG,it.data.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.textViewError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar2.isVisible = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}