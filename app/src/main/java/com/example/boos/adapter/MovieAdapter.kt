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
import com.example.boos.utili.SessionMaintainence
import com.github.islamkhsh.CardSliderAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_viewpager.view.*
import kotlinx.android.synthetic.main.item_viewpager.view.payimages
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class MovieAdapter(private val movies: MutableList<dealModel>, val activity: FragmentActivity) :
    CardSliderAdapter<MovieAdapter.MovieViewHolder>() {
    var firestoreDB: FirebaseFirestore? = null

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
        if (SessionMaintainence!!.instance!!.userType == "admin") {
            holder.imageView17.visibility = View.VISIBLE
//            holder.imageView12.visibility = View.VISIBLE
        } else {
            holder.imageView17.visibility = View.GONE
//            holder.imageView12.visibility = View.GONE
        }
        holder.imageView17.setOnClickListener {
            activity!!.alert(" Name:" + movie.name, "Are you sure delete this Name?") {
                yesButton {
                    firestoreDB = FirebaseFirestore.getInstance()

                    firestoreDB!!.collection("trend").document(movie.id)
                        .delete()
                        .addOnSuccessListener {
                            activity!!.toast("deleted successfully")
                            activity!!.finish();
                            activity!!.startActivity(activity!!.getIntent());
                        }
                        .addOnFailureListener {
                            activity!!.toast("Failed")
                        }
                }
            }.show()
        }
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val d = view.cardView5s
        val imageView17 = view.imageView17
    }

}