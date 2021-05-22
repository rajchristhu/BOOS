package com.example.boos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boos.R
import com.example.boos.activity.ItemActivity
import com.example.boos.model.dealModel
import com.github.islamkhsh.CardSliderAdapter
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_viewpager.view.*
import kotlinx.android.synthetic.main.item_viewpager.view.payimages
import org.jetbrains.anko.startActivity

class MovieAdapter(private val movies: MutableList<dealModel>, val activity: FragmentActivity) :
    CardSliderAdapter<MovieAdapter.MovieViewHolder>() {

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MovieViewHolder(view)
    }

    override fun bindVH(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.itemView.run {
//            payimages.setImageURI(movie.image)
            Glide.with(this)
                .load(movie.image)
                .placeholder(R.drawable.index)
                .into(movie_poster!!)
            movie.image
//            movie_title.text = movie.title
//            movie_overview.text = movie.overview
        }
        holder.d.setOnClickListener {
            activity!!.startActivity<ItemActivity>("id" to movie.id, "img" to movie.image)

        }
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val d = view.cardView5s
    }

}