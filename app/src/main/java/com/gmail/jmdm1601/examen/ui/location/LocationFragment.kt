package com.gmail.jmdm1601.examen.ui.location

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gmail.jmdm1601.examen.R
import com.gmail.jmdm1601.examen.databinding.FragmentLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val mBinding get() = _binding!!

    private val mLocationViewModel: LocationViewModel by viewModels()

    private val callback = OnMapReadyCallback { googleMap ->
        mLocationViewModel.result.observe(viewLifecycleOwner) {
            googleMap.clear()
            it.forEach {
                val seconds = it.date?.seconds
                var date: Date? = null
                seconds?.let {
                    date = Date(seconds * 1000)
                }
                googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            it.location!!.latitude,
                            it.location!!.longitude
                        )
                    )
                        .title(if (date != null) date.toString() else "")
                )
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.location!!.latitude, it.location!!.longitude),
                        15f
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}