package com.example.newz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newz.R
import com.example.newz.databinding.NewsListItemBinding
import com.example.newz.model.Article
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class NewsListAdapter(private val listener : NewsItemClicked) : ListAdapter<Article, NewsListAdapter.MyViewHolder>(
    NewsListDiffUtilCallBack()
) {

    class MyViewHolder(private val binding: NewsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.newsTitle
        val image = binding.newsImage
        val author = binding.newsAuthor
        val share = binding.shareBtn
        val more = binding.moreBtn
        val layout = binding.container
        val time = binding.newsTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NewsListItemBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.author.text = item.source.name
        holder.time.setText(dateTime(item.publishedAt))

        Glide.with(holder.itemView.context)
            .load(item.urlToImage)
//            .apply(RequestOptions.bitmapTransform( RoundedCorners(16)))
            .thumbnail(Glide.with(holder.image.context).load(R.drawable.spinner))
            .into(holder.image)

//        holder.like.setOnClickListener {
//            Toast.makeText(holder.like.context , "Clicked" , Toast.LENGTH_SHORT).show()
//            if(holder.like.drawable == ContextCompat.getDrawable(holder.like.context,R.drawable.ic_unliked)){
//                Toast.makeText(holder.like.context , "Image changed" , Toast.LENGTH_SHORT).show()
//                holder.like.setImageResource(R.drawable.ic_liked)
//            }else{
//                holder.like.setImageResource(R.drawable.ic_unliked)
//            }
//        }

        holder.more.setOnClickListener {
            listener.onMoreBtnClicked(item , it)
        }

        holder.layout.setOnClickListener {
            listener.onItemClicked(item)
        }

        holder.share.setOnClickListener {
            listener.onShareBtnClicked(item)
        }
    }

    fun dateTime(t : String) : String {
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
class NewsListDiffUtilCallBack : DiffUtil.ItemCallback<Article>(){
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title;
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem;
    }
}

interface NewsItemClicked{
    fun onItemClicked(item : Article)
    fun onShareBtnClicked(item : Article)
    fun onMoreBtnClicked(item : Article , view: View)
}