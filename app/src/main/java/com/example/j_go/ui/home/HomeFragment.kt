package com.example.j_go.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.j_go.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inisialisasi View Binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi elemen SearchView
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Logika saat query dikirimkan
                query?.let {
                    // TODO: Tambahkan logika pencarian
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Logika saat teks pada SearchView berubah
                newText?.let {
                    // TODO: Tambahkan logika pencarian dinamis (jika diperlukan)
                }
                return true
            }
        })

        // Inisialisasi Icon Filter
        binding.filterIcon.setOnClickListener {
            // TODO: Tambahkan logika untuk filter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
