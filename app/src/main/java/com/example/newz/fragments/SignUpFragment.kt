package com.example.newz.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.newz.R
import com.example.newz.databinding.FragmentSignUpBinding
import com.example.newz.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {

    private lateinit var binding : FragmentSignUpBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up, container, false)
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.loginTextView.setOnClickListener {
            it.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }

        binding.signUpButton.setOnClickListener {
            val name = binding.nameEdittext.text.toString()
            val email = binding.emailEdittext.text.toString()
            val password = binding.passwordEdittext.text.toString()

            if (name != "" && email != "" && password != ""){
                signUpUser(name, email, password)
            }
        }

        return binding.root
    }

    private fun  signUpUser(name:String , email : String , password : String){
        binding.spinKit.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(requireContext(),"SignUp Successful",Toast.LENGTH_SHORT).show()
                    val currentUser = auth.currentUser
                    database.child("users").child(currentUser!!.uid)
                        .setValue(User(
                            name,currentUser.uid
                        ))
                    binding.spinKit.visibility = View.GONE

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignUpFragment", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    binding.spinKit.visibility = View.GONE
                }
            }
    }

    private fun addUserToDatabase(name : String , id : String){
        
    }


}