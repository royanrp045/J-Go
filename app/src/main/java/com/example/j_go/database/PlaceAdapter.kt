package com.example.j_go.database

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.j_go.databinding.ItemPlaceBinding

class PlaceAdapter(private val places: List<Place>) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    // Interface listener untuk menangani klik item
    private var onItemClickListener: ((Place) -> Unit)? = null

    // Fungsi untuk set listener
    fun setOnItemClickListener(listener: (Place) -> Unit) {
        onItemClickListener = listener
    }

    // ViewHolder yang berisi binding untuk item
    class PlaceViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    // Membuat view holder untuk setiap item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    // Mengikat data ke setiap item di RecyclerView
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.binding.apply {
            textName.text = place.place_name
            textCategory.text = place.category
            textPrice.text = "Rp ${place.price}"
            textRate.text = "Rating: ${place.place_rate}"
            imagePlace.setImageResource(
                root.context.resources.getIdentifier(
                    place.image_res, "drawable", root.context.packageName
                )
            )
        }

        // Menangani klik item untuk mengarahkan ke DetailActivity
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(place)
        }
    }

    // Mengembalikan jumlah item dalam RecyclerView
    override fun getItemCount(): Int {
        return places.size
    }
}