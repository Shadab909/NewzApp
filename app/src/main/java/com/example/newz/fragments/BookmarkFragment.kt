package com.example.newz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.newz.R
import com.example.newz.databinding.FragmentBookmarkBinding


class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bookmark, container, false)


        binding.loginBtn.setOnClickListener {
            
        }


        return binding.root
    }

}