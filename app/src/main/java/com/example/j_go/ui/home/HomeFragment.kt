package com.example.j_go.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.j_go.R
import com.example.j_go.database.Place
import com.example.j_go.databinding.FragmentHomeBinding
import com.example.j_go.ui.filter.FilterActivity
import com.example.j_go.ui.detail.DetailActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import java.io.InputStreamReader

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private val wisataList = mutableListOf<Place>()
    private var lastClickedMarker: Marker? = null // Untuk menyimpan marker terakhir yang diklik

    private val filterLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
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

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchPlaces(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchPlaces(it)
                }
                return true
            }
        })

        loadWisataData()
    }

    private fun searchPlaces(query: String) {
        val filteredList = wisataList.filter { place ->
            place.place_name.contains(query, ignoreCase = true) ||
                    place.description_indonesia.contains(query, ignoreCase = true)
        }
        updateMapMarkers(filteredList)
    }


    private fun loadWisataData() {
        val inputStream = resources.openRawResource(R.raw.data_wisata)
        val reader = InputStreamReader(inputStream)
        val wisataArray = Gson().fromJson(reader, Array<Place>::class.java)
        wisataList.addAll(wisataArray)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        updateMapMarkers(wisataList)

        // Set listener untuk klik marker
        googleMap.setOnMarkerClickListener { marker ->
            lastClickedMarker = marker
            // Menampilkan informasi ringkas atau highlight dari tempat
            marker.showInfoWindow() // Menampilkan info window saat marker diklik
            true
        }

        // Set listener untuk klik info window (highlight)
        googleMap.setOnInfoWindowClickListener { marker ->
            val place = wisataList.find { it.place_name == marker.title }
            place?.let {
                // Kirim data Place ke DetailActivity
                val intent = Intent(requireContext(), DetailActivity::class.java)
                val placeJson = Gson().toJson(it) // Mengubah objek Place menjadi JSON
                intent.putExtra("PLACE_DATA", placeJson)
                startActivity(intent)
            }
        }
    }

    private fun applyFilter(category: String?, minPrice: Int, maxPrice: Int, rate: Double) {
        val filteredList = wisataList.filter { wisata ->
            (category == null || wisata.category == category) &&
                    wisata.price in minPrice..maxPrice &&
                    wisata.place_rate >= rate
        }
        updateMapMarkers(filteredList)
    }

    private fun updateMapMarkers(filteredList: List<Place>) {
        googleMap.clear()
        val boundsBuilder = LatLngBounds.Builder()
        for (wisata in filteredList) {
            val lokasi = LatLng(wisata.latitude, wisata.longitude)
            googleMap.addMarker(
                MarkerOptions()
                    .position(lokasi)
                    .title(wisata.place_name)
                    .snippet(wisata.description_indonesia)
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