package com.example.j_go.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.j_go.database.FeedbackDatabaseHelper
import com.example.j_go.databinding.ActivityDetailBinding
import com.example.j_go.database.Place
import com.google.gson.Gson

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var place: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari intent
        val placeJson = intent.getStringExtra("PLACE_DATA")
        place = Gson().fromJson(placeJson, Place::class.java)

        // Tampilkan data
        displayPlaceData()

        // Siapkan spinner untuk mengganti deskripsi
        setupSpinner()

        // Set tombol back untuk kembali ke halaman sebelumnya
        binding.imageView.setOnClickListener {
            onBackPressed()
        }

        // Ambil userId dari SharedPreferences
        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", "") ?: ""

        // Cek apakah user sudah pernah memberikan rating untuk tempat ini
        val hasRated = sharedPreferences.getBoolean("has_rated_${place.place_name}", false)

        // Menonaktifkan tombol jika sudah memberikan rating
        if (hasRated) {
            binding.buttonRate.isEnabled = false
            binding.buttonRate.text = "Rating Submitted"
        }

        // Simpan feedback ke database saat tombol submit ditekan
        binding.button.setOnClickListener {
            val feedbackText = binding.editText.text.toString()
            if (feedbackText.isNotEmpty()) {
                val feedbackDatabaseHelper = FeedbackDatabaseHelper(this)
                feedbackDatabaseHelper.addFeedback(place.place_name, feedbackText, userId) // Menambahkan userId
                Toast.makeText(this, "Feedback submitted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter feedback.", Toast.LENGTH_SHORT).show()
            }
        }

        // Menangani tombol rate this place
        binding.buttonRate.setOnClickListener {
            // Ambil rating dari RatingBar
            val rating = binding.ratingBar.rating

            if (rating > 0) {
                // Tampilkan rating yang diberikan menggunakan Toast
                Toast.makeText(this, "You rated this place: $rating stars", Toast.LENGTH_SHORT).show()

                // Menyimpan status rating sudah diberikan
                sharedPreferences.edit().apply {
                    putBoolean("has_rated_${place.place_name}", true) // Menyimpan status rating
                    apply()
                }

                // Menonaktifkan tombol setelah rating diberikan
                binding.buttonRate.isEnabled = false
                binding.buttonRate.text = "Rating Submitted"
            } else {
                // Tampilkan pesan jika rating belum diberikan
                Toast.makeText(this, "Please select a rating!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayPlaceData() {
        binding.apply {
            textView.text = place.place_name
            textView2.text = "Rating: ${place.place_rate}"
            textView3.text = "Rp ${place.price}"
            textView4.text = place.category
            imageView.setImageResource(
                resources.getIdentifier(place.image_res, "drawable", packageName)
            )
            descriptionTextView.text = place.description_english // Default English
        }
    }

    private fun setupSpinner() {
        val languages = arrayOf("Select Language", "English", "Indonesia")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (languages[position]) {
                    "Indonesia" -> binding.descriptionTextView.text = place.description_indonesia
                    "English" -> binding.descriptionTextView.text = place.description_english
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}