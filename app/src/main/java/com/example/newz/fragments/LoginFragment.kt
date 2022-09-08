package com.example.newz.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.newz.R
import com.example.newz.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)
        auth = Firebase.auth
        navController = findNavController(this)

        binding.signupTextview.setOnClickListener {
            it.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }

        binding.backButton.setOnClickListener {
            it.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEdittext.text.toString()
            val password = binding.passwordEdittext.text.toString()

            if (email != "" && password != ""){
                loginUser(email,password)
            }
        }

        binding.forgetPassword.setOnClickListener {
            it.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgetFragment())
        }

        return binding.root
    }

    private fun loginUser(email : String , password : String){
        binding.spinKit.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    binding.spinKit.visibility = View.GONE
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginFragment", "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT).show()

                    binding.spinKit.visibility = View.GONE

                }
            }
    }
}