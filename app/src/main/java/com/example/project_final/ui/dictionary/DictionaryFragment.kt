package com.example.project_final.ui.dictionary
import DictionaryAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_final.api.RandomWordApiHandler
import com.example.project_final.databinding.FragmentDictionaryBinding

class DictionaryFragment : Fragment() {

    private var _binding: FragmentDictionaryBinding? = null
    private val sharedPreferencesFileName = "mySharedPreferences"
    private val stringSetKey = "myStringSet"
    private val randomWordApiHandler = RandomWordApiHandler()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPreferences = requireActivity().getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)

        val tmp = sharedPreferences.getString(stringSetKey, "")
        val words = tmp!!.split(",")

        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        val adapter = DictionaryAdapter(words)
        binding.recyclerView.adapter = adapter

        binding.button.setOnClickListener {
            updateSharePrefenerces()
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }

        return root
    }

    private fun updateSharePrefenerces() {
        randomWordApiHandler.fetchRandomWord { response ->
            if (response != null) {
                val sharedPreferences = requireActivity().getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
                var responseSet = response.joinToString(",")
                if (sharedPreferences.contains(stringSetKey)) {
                    val oldData = sharedPreferences.getString(stringSetKey, "")
                    responseSet = "$oldData,$responseSet"
                }
                sharedPreferences.edit().putString(stringSetKey, responseSet).apply()
                println(response)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}