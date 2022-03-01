package com.gmail.jmdm1601.examen.ui.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.jmdm1601.examen.R
import com.gmail.jmdm1601.examen.core.Constants
import com.gmail.jmdm1601.examen.data.model.Movie
import com.gmail.jmdm1601.examen.databinding.ItemMovieBinding

class MoviesAdapter(private var data: List<Movie>, private val listener: OnClickListener) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = data[position]

        holder.bind(movie)
    }

    override fun getItemCount(): Int = data.size

    fun setMovies(movies: List<Movie>) {
        this.data = movies
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(movie: Movie) {
            itemBinding.tvTitle.text = movie.title

            Glide.with(context)
                .load("${Constants.BASE_URL_IMAGES}${movie.posterPath}")
                .error(R.drawable.ic_image_not_supported)
                .centerCrop()
                .into(itemBinding.imgPhoto)

            itemBinding.root.setOnClickListener {
                listener.onClick(movie)
            }
        }
    }
}