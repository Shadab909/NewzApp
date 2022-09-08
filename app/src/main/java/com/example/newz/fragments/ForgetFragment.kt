package com.example.newz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.newz.R
import com.example.newz.databinding.FragmentForgetBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ForgetFragment : Fragment() {
    private lateinit var binding : FragmentForgetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_forget, container, false)

        binding.btnSubmit.setOnClickListener {
            val email = binding.etEmail.text.toString().trim(){it <= ' '}
            if (email == ""){
                Toast.makeText(requireContext(), "Please enter email address", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{ task->
                        if (task.isSuccessful){
                            Toast.makeText(requireContext()
                                , "Email sent successfully to reset your password"
                                , Toast.LENGTH_SHORT)
                                .show()
                            it.findNavController().popBackStack()
                        }else{
                            Toast.makeText(requireContext()
                                , task.exception!!.message.toString()
                                , Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }

        return binding.root
    }


}