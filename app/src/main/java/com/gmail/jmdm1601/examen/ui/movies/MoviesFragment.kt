package com.gmail.jmdm1601.examen.ui.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gmail.jmdm1601.examen.R
import com.gmail.jmdm1601.examen.core.Resource
import com.gmail.jmdm1601.examen.data.model.Movie
import com.gmail.jmdm1601.examen.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(), OnClickListener {

    private var _binding: FragmentMoviesBinding? = null
    private val mBinding get() = _binding!!

    private val mMoviesViewModel: MoviesViewModel by viewModels()
    private val mAdapter: MoviesAdapter = MoviesAdapter(mutableListOf(), this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        mBinding.recyclerView.apply {
            layoutManager =
                GridLayoutManager(context, resources.getInteger(R.integer.span_count_grid))
            adapter = mAdapter
        }
    }

    private fun setupViewModel() {
        mMoviesViewModel.movies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> mBinding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    mBinding.progressBar.visibility = View.GONE
                    mAdapter.setMovies(it.data)
                }
                else -> mBinding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onClick(movie: Movie) {
        val action = MoviesFragmentDirections.moviesToMovieDetailFragment(movie.id, movie.title)
        findNavController().navigate(action)
    }
}