package com.example.j_go.ui.filter

import android.graphics.Color
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.j_go.databinding.ActivityFilterBinding
import com.google.android.material.chip.Chip

class FilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCategoryFilter()
        setupRateFilter()
        setupPriceFilter()
        setupResetButton()
    }

    private fun setupCategoryFilter() {
        binding.categoryGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            selectedRadioButton?.let {
                addChipToAppliedFilters(it.text.toString())
            }
        }
    }

    private fun setupRateFilter() {
        val rateButtons = listOf(
            binding.button5Star,
            binding.button4Star,
            binding.button3Star,
            binding.button2Star,
            binding.button1Star
        )

        rateButtons.forEach { button ->
            button.setOnClickListener {
                // Reset background color for all buttons
                rateButtons.forEach { btn ->
                    btn.setBackgroundColor(Color.GRAY)
                }
                // Highlight the selected button
                button.setBackgroundColor(Color.BLUE)
                addChipToAppliedFilters(button.text.toString())
            }
        }
    }

    private fun setupPriceFilter() {
        val minPrice = binding.minPrice.text.toString()
        val maxPrice = binding.maxPrice.text.toString()

        if (minPrice.isNotEmpty() && maxPrice.isNotEmpty()) {
            addChipToAppliedFilters("Price: $minPrice - $maxPrice")
        }

        binding.minPrice.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                checkPrice()
            }
        }

        binding.maxPrice.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                checkPrice()
            }
        }
    }

    private fun checkPrice() {
        val minPrice = binding.minPrice.text.toString()
        val maxPrice = binding.maxPrice.text.toString()

        if (minPrice.isNotEmpty() && maxPrice.isNotEmpty()) {
            addChipToAppliedFilters("Price: $minPrice - $maxPrice")
        }
    }

    private fun addChipToAppliedFilters(filterText: String) {
        // Avoid duplicate chips
        val existingChip = binding.appliedFiltersGroup.findViewWithTag<Chip>(filterText)
        if (existingChip == null) {
            val chip = Chip(this).apply {
                text = filterText
                tag = filterText
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    binding.appliedFiltersGroup.removeView(this)
                }
            }
            binding.appliedFiltersGroup.addView(chip)
        }
    }

    private fun setupResetButton() {
        binding.resetButton.setOnClickListener {
            // Clear all chips
            binding.appliedFiltersGroup.removeAllViews()
            // Reset price fields
            binding.minPrice.text.clear()
            binding.maxPrice.text.clear()
            // Reset rate buttons
            resetRateButtons()
        }
    }

    private fun resetRateButtons() {
        val rateButtons = listOf(
            binding.button5Star,
            binding.button4Star,
            binding.button3Star,
            binding.button2Star,
            binding.button1Star
        )
        rateButtons.forEach { button ->
            button.setBackgroundColor(Color.GRAY)
        }
    }
}
