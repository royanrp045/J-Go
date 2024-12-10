package com.example.j_go.ui.filter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.j_go.R
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
        setupUseButton()
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
                rateButtons.forEach { btn -> btn.setBackgroundColor(Color.GRAY) }
                button.setBackgroundColor(Color.BLUE)
                addChipToAppliedFilters(button.text.toString())
            }
        }
    }

    private fun setupPriceFilter() {
        binding.minPrice.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) checkPrice() }
        binding.maxPrice.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) checkPrice() }
    }

    private fun checkPrice() {
        val minPrice = binding.minPrice.text.toString()
        val maxPrice = binding.maxPrice.text.toString()
        if (minPrice.isNotEmpty() && maxPrice.isNotEmpty()) {
            addChipToAppliedFilters("Price: $minPrice - $maxPrice")
        }
    }

    private fun setupUseButton() {
        binding.useButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("category", getSelectedCategory())
            intent.putExtra("minPrice", binding.minPrice.text.toString().toIntOrNull() ?: 0)
            intent.putExtra("maxPrice", binding.maxPrice.text.toString().toIntOrNull() ?: Int.MAX_VALUE)
            intent.putExtra("rate", getSelectedRate())
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun getSelectedCategory(): String? {
        val checkedId = binding.categoryGroup.checkedRadioButtonId
        return if (checkedId != -1) {
            findViewById<RadioButton>(checkedId)?.text.toString()
        } else null
    }

    private fun getSelectedRate(): Double {
        return when {
            binding.button5Star.backgroundTintList == getColorStateList(R.color.main) -> 5.0
            binding.button4Star.backgroundTintList == getColorStateList(R.color.main) -> 4.0
            binding.button3Star.backgroundTintList == getColorStateList(R.color.main) -> 3.0
            binding.button2Star.backgroundTintList == getColorStateList(R.color.main) -> 2.0
            binding.button1Star.backgroundTintList == getColorStateList(R.color.main) -> 1.0
            else -> 0.0
        }
    }

    private fun setupResetButton() {
        binding.resetButton.setOnClickListener {
            binding.appliedFiltersGroup.removeAllViews()
            binding.minPrice.text.clear()
            binding.maxPrice.text.clear()
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
        rateButtons.forEach { button -> button.setBackgroundColor(Color.GRAY) }
    }

    private fun addChipToAppliedFilters(filterText: String) {
        val existingChip = binding.appliedFiltersGroup.findViewWithTag<Chip>(filterText)
        if (existingChip == null) {
            val chip = Chip(this).apply {
                text = filterText
                tag = filterText
                isCloseIconVisible = true
                setOnCloseIconClickListener { binding.appliedFiltersGroup.removeView(this) }
            }
            binding.appliedFiltersGroup.addView(chip)
        }
    }
}
