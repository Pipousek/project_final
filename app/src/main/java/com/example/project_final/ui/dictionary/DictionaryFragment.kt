package com.example.project_final.ui.dictionary
import DictionaryAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_final.databinding.FragmentDictionaryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            lifecycleScope.launch {
                // Use the async function within a coroutine scope
                val updatedList = dictioanryViewModel.updateSharePrefenerces()

                // Update the RecyclerView on the main thread
                withContext(Dispatchers.Main) {
                    updateRecyclerView(updatedList)
                }
            }
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