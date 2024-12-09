package com.example.j_go.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    // Data wisata yang akan di-load
    private val wisataList = mutableListOf<Wisata>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Menggunakan loadWisataData untuk memuat data dari raw
    private fun loadWisataData() {
        val inputStream = resources.openRawResource(R.raw.data_wisata) // Pastikan file data_wisata.json ada di folder raw
        val reader = InputStreamReader(inputStream)
        val wisataArray = Gson().fromJson(reader, Array<Wisata>::class.java)
        wisataList.addAll(wisataArray)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Map
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // SearchView logic
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Implement search logic
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Implement dynamic search logic
                return true
            }
        })

        // Filter icon logic
        binding.filterIcon.setOnClickListener {
            val intent = Intent(requireContext(), FilterActivity::class.java)
            startActivity(intent)
        }

        // Load JSON Data
        loadWisataData()  // Panggil hanya sekali di onViewCreated
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Tambahkan marker untuk setiap lokasi wisata
        val boundsBuilder = LatLngBounds.Builder() // Untuk menentukan batas peta
        for (wisata in wisataList) {
            val lokasi = LatLng(wisata.latitude, wisata.longitude)
            googleMap.addMarker(
                MarkerOptions()
                    .position(lokasi)
                    .title(wisata.name)
                    .snippet(wisata.description)
            )
            boundsBuilder.include(lokasi) // Tambahkan setiap lokasi ke dalam bounds
        }

        // Pusatkan kamera di antara semua lokasi
        val bounds = boundsBuilder.build()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)) // 100 adalah padding
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
