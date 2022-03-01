package com.gmail.jmdm1601.examen.ui.movies.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gmail.jmdm1601.examen.R
import com.gmail.jmdm1601.examen.core.Constants
import com.gmail.jmdm1601.examen.core.Resource
import com.gmail.jmdm1601.examen.data.model.Movie
import com.gmail.jmdm1601.examen.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val mBinding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()
    private val mMovieDetailViewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.idMovie
        setupViewModel()
        mMovieDetailViewModel.getMovie(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        mMovieDetailViewModel.movie.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> mBinding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    mBinding.progressBar.visibility = View.GONE
                    configData(it.data)
                }
                else -> mBinding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun configData(movie: Movie) {
        mBinding.tvTitle.text = movie.title
        mBinding.tvDescription.text = movie.overview
        val parser = SimpleDateFormat(getString(R.string.default_format_date), Locale.getDefault())
        val formatter =
            SimpleDateFormat(getString(R.string.format_release_date), Locale.getDefault())
        mBinding.tvReleaseDate.text = formatter.format(parser.parse(movie.releaseDate)!!)
        mBinding.ratingBar.rating = (movie.voteAverage / 2).toFloat()
        Glide.with(requireActivity())
            .load("${Constants.BASE_URL_IMAGES}${movie.backdropPath}")
            .error(R.drawable.ic_image_not_supported)
            .into(mBinding.imgBackdrop)

    }
}