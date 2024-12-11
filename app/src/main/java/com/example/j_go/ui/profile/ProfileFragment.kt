package com.example.j_go.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.j_go.database.Feedback
import com.example.j_go.database.FeedbackDatabaseHelper
import com.example.j_go.databinding.FragmentProfileBinding
import com.example.j_go.databinding.ItemHistoryBinding
import com.example.j_go.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var feedbackDatabaseHelper: FeedbackDatabaseHelper
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data feedback dari database
        feedbackDatabaseHelper = FeedbackDatabaseHelper(requireContext())
        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", "") ?: ""
        val feedbackList = feedbackDatabaseHelper.getAllFeedbacksForUser(userId)

        // Set RecyclerView
        val adapter = FeedbackAdapter(feedbackList)
        binding.recyclerViewFeedback.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFeedback.adapter = adapter

        // Ambil data pengguna dari shared preferences
        val name = sharedPreferences.getString("name", "No name")
        val email = sharedPreferences.getString("email", "No email")

        binding.nama.text = name
        binding.nim.text = email

        // Set up logout button click listener
        binding.btnLogout.setOnClickListener {
            // Setelah logout, arahkan pengguna ke halaman LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()  // Menutup activity ini
        }
    }

    inner class FeedbackAdapter(private val feedbackList: List<Feedback>) :
        RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
            val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FeedbackViewHolder(binding)
        }

        override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
            val feedback = feedbackList[position]
            holder.binding.tvPlaceName.text = feedback.placeName
            holder.binding.tvFeedbackContent.text = feedback.feedback
        }

        override fun getItemCount(): Int = feedbackList.size

        inner class FeedbackViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)
    }
}
