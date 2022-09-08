package com.example.newz.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.newz.R
import com.example.newz.adapters.BookMarkNewsListAdapter
import com.example.newz.databinding.FragmentBookmarkBinding
import com.example.newz.model.BookMarkNews
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.math.log


class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var list: ArrayList<BookMarkNews>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)
        auth = Firebase.auth
        databaseRef = Firebase.database.reference
        list = ArrayList()


        binding.loginBtn.setOnClickListener {
            it.findNavController()
                .navigate(BookmarkFragmentDirections.actionBookmarkFragment2ToLoginFragment2())
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        populateRecyclerView()


        return binding.root
    }

    private fun populateRecyclerView() {
        val adapter = BookMarkNewsListAdapter()
        binding.bookmarkNewsRv.adapter = adapter
        binding.spinKit.visibility = View.VISIBLE

        databaseRef.child("news")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (news in snapshot.children) {

                        news.getValue<BookMarkNews>()?.let { list.add(it)}
                    }
                    adapter.submitList(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("BookMarkFragment", "onCancelled: ${error.message} ")
                }

            })
        binding.spinKit.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null) {
            binding.loginBtn.visibility = View.GONE
            binding.textView.visibility = View.GONE
        } else {
            binding.loginBtn.visibility = View.VISIBLE
            binding.textView.visibility = View.VISIBLE
        }
    }

}