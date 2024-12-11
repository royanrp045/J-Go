package com.example.j_go.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.j_go.R
import com.example.j_go.database.PlaceAdapter
import com.example.j_go.database.loadJsonFromRaw
import com.example.j_go.databinding.FragmentDashboardBinding
import com.example.j_go.ui.detail.DetailActivity
import com.google.gson.Gson

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Load data from res/raw/data_wisata.json
        val places = loadJsonFromRaw(requireContext(), R.raw.data_wisata)

        // Set up RecyclerView
        val adapter = PlaceAdapter(places)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Set up item click listener
        adapter.setOnItemClickListener { place ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("PLACE_DATA", Gson().toJson(place))
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}