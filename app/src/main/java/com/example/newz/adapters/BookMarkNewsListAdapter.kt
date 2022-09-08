package com.example.newz.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newz.R
import com.example.newz.databinding.BookmarkNewsListItemBinding
import com.example.newz.model.BookMarkNews
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class BookMarkNewsListAdapter() : ListAdapter<BookMarkNews, BookMarkNewsListAdapter.MyViewHolder>(
    BookMarkNewsListDiffUtilCallBack()
) {
    class MyViewHolder(binding: BookmarkNewsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.newsTitle
        val image = binding.newsImage
        val author = binding.newsAuthor
        val share = binding.shareBtn
        val layout = binding.container
        val time = binding.newsTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BookmarkNewsListItemBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.author.text = item.source?.name
        holder.time.setText(item.publishedAt?.let { dateTime(it) })

        Glide.with(holder.itemView.context)
            .load(item.urlToImage)
            .thumbnail(Glide.with(holder.image.context).load(R.drawable.loading))
            .into(holder.image)

        holder.layout.setOnClickListener {
            val builder =  CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            holder.layout.context?.let { customTabsIntent.launchUrl(it, Uri.parse(item.url)) }
        }

        holder.share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, item.url)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            holder.share.context.startActivity(shareIntent)
        }
    }

    fun dateTime(t : String) : String{
        val prettyTime = PrettyTime(Locale.getDefault().country.lowercase(Locale.getDefault()))
        var time : String = ""
        try{
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:")
            val date = simpleDateFormat.parse(t)
            time = prettyTime.format(date)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return time;
    }
}

class BookMarkNewsListDiffUtilCallBack : DiffUtil.ItemCallback<BookMarkNews>(){
    override fun areItemsTheSame(oldItem: BookMarkNews, newItem: BookMarkNews): Boolean {
        return oldItem.url == newItem.url;
    }

    override fun areContentsTheSame(oldItem: BookMarkNews, newItem: BookMarkNews): Boolean {
        return oldItem == newItem;
    }
}