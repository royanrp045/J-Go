package com.example.j_go.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.j_go.R
import com.example.j_go.database.Wisata
import com.example.j_go.databinding.FragmentHomeBinding
import com.example.j_go.ui.filter.FilterActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import java.io.InputStreamReader

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private val wisataList = mutableListOf<Wisata>()

    private val filterLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) { // Perbaikan ada di sini
                val data = result.data
                val category = data?.getStringExtra("category")
                val minPrice = data?.getIntExtra("minPrice", 0) ?: 0
                val maxPrice = data?.getIntExtra("maxPrice", Int.MAX_VALUE) ?: Int.MAX_VALUE
                val rate = data?.getDoubleExtra("rate", 0.0) ?: 0.0
                applyFilter(category, minPrice, maxPrice, rate)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.filterIcon.setOnClickListener {
            val intent = Intent(requireContext(), FilterActivity::class.java)
            filterLauncher.launch(intent)
        }

        loadWisataData()
    }

    private fun loadWisataData() {
        val inputStream = resources.openRawResource(R.raw.data_wisata)
        val reader = InputStreamReader(inputStream)
        val wisataArray = Gson().fromJson(reader, Array<Wisata>::class.java)
        wisataList.addAll(wisataArray)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        updateMapMarkers(wisataList)
    }

    private fun applyFilter(category: String?, minPrice: Int, maxPrice: Int, rate: Double) {
        val filteredList = wisataList.filter { wisata ->
            (category == null || wisata.category == category) &&
                    wisata.ticket_price in minPrice..maxPrice &&
                    wisata.rate >= rate
        }
        updateMapMarkers(filteredList)
    }

    private fun updateMapMarkers(filteredList: List<Wisata>) {
        googleMap.clear()
        val boundsBuilder = LatLngBounds.Builder()
        for (wisata in filteredList) {
            val lokasi = LatLng(wisata.latitude, wisata.longitude)
            googleMap.addMarker(
                MarkerOptions()
                    .position(lokasi)
                    .title(wisata.name)
                    .snippet(wisata.description)
            )
            boundsBuilder.include(lokasi)
        }
        if (filteredList.isNotEmpty()) {
            val bounds = boundsBuilder.build()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
