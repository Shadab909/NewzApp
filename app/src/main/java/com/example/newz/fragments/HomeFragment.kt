package com.example.newz.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newz.R
import com.example.newz.adapters.NewsItemClicked
import com.example.newz.adapters.NewsListAdapter
import com.example.newz.databinding.FragmentHomeBinding
import com.example.newz.model.Article
import com.example.newz.model.BookMarkNews
import com.example.newz.model.BookMarkNewsSource
import com.example.newz.util.MyUtil
import com.example.newz.viewmodel.HomeFragmentViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() , NewsItemClicked  {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mNewsListAdapter: NewsListAdapter
    private lateinit var viewModel: HomeFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        mNewsListAdapter = NewsListAdapter(this)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        val position = requireArguments().getInt("position")
        val category = getCategory(position)
        val country = "in"
        initNewsRecyclerview()
        showData(category, country)

        binding.swipeRefresh.setOnRefreshListener {
            showData(category, country)
            binding.swipeRefresh.isRefreshing = false;
        }


        return binding.root
    }

    private fun showData(category : String , country : String )
    {
        if(MyUtil.isInternetAvailable(requireContext())){
            binding.noInternet.visibility = View.GONE
            binding.newsListRv.visibility = View.VISIBLE
            getData(country,category)
        }else{
            binding.noInternet.visibility = View.VISIBLE
            binding.spinKit.visibility = View.GONE
            binding.newsListRv.visibility = View.GONE

        }
    }
    private fun initNewsRecyclerview(){
        binding.newsListRv.apply {
            layoutManager = LinearLayoutManager(context)
            mNewsListAdapter = NewsListAdapter(this@HomeFragment)
            adapter = mNewsListAdapter
        }
    }

    private fun getData(country : String ,category : String){
        binding.spinKit.visibility = View.VISIBLE
        binding.newsListRv.visibility = View.GONE
        viewModel.getNewsListDataObserver().observe(viewLifecycleOwner) {

            mNewsListAdapter.submitList(it) {
                //runnable lambda expression
                binding.newsListRv.scrollToPosition(0)
            }
            binding.newsListRv.visibility = View.VISIBLE
            binding.spinKit.visibility = View.GONE
        }
        viewModel.makeApiCall(country,category)
    }

    override fun onItemClicked(item: Article) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        context?.let { customTabsIntent.launchUrl(it, Uri.parse(item.url)) }
    }

    override fun onShareBtnClicked(item: Article) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, item.url)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onMoreBtnClicked(item: Article , view: View) {
        val popupMenu = PopupMenu(context , view)
        popupMenu.menuInflater.inflate(R.menu.add_more_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked
            if (menuItem.itemId == R.id.add_to_bookmark){
                val database = Firebase.database.reference
                val time  = System.currentTimeMillis()
                val news = BookMarkNews(item.author,item.content,item.description,
                    item.publishedAt,BookMarkNewsSource(item.source.id,item.source.name),item.title,item.url,item.urlToImage)
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null){
                    database.child("news")
                        .child(currentUser.uid).child(time.toString())
                        .setValue(news)
                }else{
                    Toast.makeText(requireContext(), "Please Login to bookmark news", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun getCategory(position : Int) : String{
        return when(position){
            0 -> "general"
            1-> "entertainment"
            2-> "health"
            3-> "business"
            4-> "science"
            5-> "sports"
            6-> "technology"
            else-> "null"
        }
    }
}