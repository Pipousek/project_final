package com.example.project_final.ui.dictionary
import DictionaryAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_final.api.RandomWordApiHandler
import com.example.project_final.databinding.FragmentDictionaryBinding

class DictionaryFragment : Fragment() {

    private var _binding: FragmentDictionaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dictioanryViewModel =
            ViewModelProvider(this).get(DictionaryViewModel::class.java)

        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        updateRecyclerView(dictioanryViewModel.returnWordsAsList())

        binding.button.setOnClickListener {
            dictioanryViewModel.updateSharePrefenerces()
            updateRecyclerView(dictioanryViewModel.returnWordsAsList())
        }

        return root
    }

    private fun updateRecyclerView(words: List<String>) {
        val adapter = DictionaryAdapter(words)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}