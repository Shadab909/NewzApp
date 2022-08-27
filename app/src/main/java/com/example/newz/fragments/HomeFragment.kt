package com.example.newz.fragments

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.widget.PopupMenu
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newz.R
import com.example.newz.adapters.CategoryAdapter
import com.example.newz.adapters.CategoryClickListener
import com.example.newz.adapters.NewsItemClicked
import com.example.newz.adapters.NewsListAdapter
import com.example.newz.databinding.FragmentHomeBinding
import com.example.newz.model.Article
import com.example.newz.viewmodel.HomeFragmentViewModel


class HomeFragment : Fragment() , NewsItemClicked , CategoryClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mCategoryAdapter: CategoryAdapter
    private lateinit var mNewsListAdapter: NewsListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        mCategoryAdapter = CategoryAdapter(this)
        mNewsListAdapter = NewsListAdapter(this)
        initCategoryRecyclerView()
        val category = "entertainment"
        val country = "in"
        initNewsRecyclerview()
        getData(country,category)
        return binding.root
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
        val viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
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

    private fun initCategoryRecyclerView() {
        val categoryList = listOf(
            "entertainment",
            "general",
            "health",
            "business",
            "science",
            "sports",
            "technology"
        )
        binding.categoryRv.adapter = mCategoryAdapter
        mCategoryAdapter.submitList(categoryList)
    }

    override fun onClick(item: String) {
        getData(country = "in",item)
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
                Toast.makeText(context,"added",Toast.LENGTH_SHORT).show()
            }
            true
        }
        popupMenu.show()
    }


}