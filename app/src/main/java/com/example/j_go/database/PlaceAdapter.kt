package com.example.j_go.database

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.j_go.databinding.ItemPlaceBinding

class PlaceAdapter(private val places: List<Place>) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.binding.textName.text = place.place_name
        holder.binding.textCategory.text = place.category
        holder.binding.textPrice.text = "Rp ${place.price}"
        holder.binding.textRate.text = "Rating: ${place.place_rate}"
        holder.binding.imagePlace.setImageResource(
            holder.itemView.context.resources.getIdentifier(
                place.image_res, "drawable", holder.itemView.context.packageName
            )
        )
    }

    override fun getItemCount() = places.size
}
