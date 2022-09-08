package com.example.newz.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.example.newz.R
import com.example.newz.adapters.ViewPagerAdapter
import com.example.newz.databinding.FragmentMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainFragment : Fragment() , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding : FragmentMainBinding
    private lateinit var viewPager : ViewPager2
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var navController: NavController
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false)
        viewPager = binding.appBar.mainContent.viewpager
        pagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle)
        auth = Firebase.auth
        navController = findNavController(this)
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.appBar.toolbar)

        val toggle = ActionBarDrawerToggle(requireActivity(),binding.drawerLayout,binding.appBar.toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled
        toggle.syncState()


        binding.navView.setNavigationItemSelectedListener(this)

        viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.appBar.tabs, viewPager) { tab, position ->
            tab.text = tabTitles(position)
        }.attach()

        binding.navView.getHeaderView(0).findViewById<Button>(R.id.btn_login).setOnClickListener{
            it.findNavController().navigate(MainFragmentDirections.actionMainFragmentToLoginFragment())
        }

        return binding.root
    }

    private fun tabTitles(position : Int) : String{
        return when(position){
            0-> getString(R.string.home)
            1-> getString(R.string.entertainment)
            2-> getString(R.string.health)
            3-> getString(R.string.business)
            4-> getString(R.string.science)
            5-> getString(R.string.sports)
            6-> getString(R.string.technology)
            else-> getString(R.string.home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.homeFragment -> viewPager.currentItem = 0
            R.id.entertainmentFragment -> viewPager.currentItem = 1
            R.id.healthFragment -> viewPager.currentItem = 2
            R.id.businessFragment -> viewPager.currentItem = 3
            R.id.scienceFragment -> viewPager.currentItem = 4
            R.id.sportsFragment -> viewPager.currentItem = 5
            R.id.technologyFragment -> viewPager.currentItem = 6
            R.id.bookmarkFragment2 ->{
                navController.navigate(MainFragmentDirections.actionMainFragmentToBookmarkFragment2())
            }
            R.id.signOut ->{
                auth.signOut()
                binding.navView.getHeaderView(0).apply {
                    findViewById<TextView>(R.id.user_name).visibility = View.GONE
                    findViewById<Button>(R.id.btn_login).visibility = View.VISIBLE
                }
            }
            else-> viewPager.currentItem = 0
        }
        binding.drawerLayout.close()
        return true
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user != null){
            binding.navView.getHeaderView(0).apply {
                findViewById<TextView>(R.id.user_name).apply {
                    visibility = View.VISIBLE
                    val database  = Firebase.database.reference
                    database.child("users").child(user.uid).child("userName").get().addOnSuccessListener {
                        text = it.value.toString()
                    }
                }
                findViewById<Button>(R.id.btn_login).visibility = View.GONE
            }
        }
    }

}