package com.example.project_final.ui.game

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project_final.R
import com.example.project_final.databinding.FragmentGameBinding
import com.example.project_final.getSharedPreferencesFileName
import com.example.project_final.getStringSetKey

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val sharedPreferencesFileName = getSharedPreferencesFileName()
    private val stringSetKey = getStringSetKey()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val editTexts = mutableListOf<EditText>()
    private val circles = mutableListOf<ImageView>()

    private var origWord = ""
    private var guessWord = ""
    private var attempt = 0

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gameViewModel =
            ViewModelProvider(this).get(GameViewModel::class.java)

        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        editTexts.add(binding.editText1)
        editTexts.add(binding.editText2)
        editTexts.add(binding.editText3)
        editTexts.add(binding.editText4)
        editTexts.add(binding.editText5)

        circles.add(binding.circle1)
        circles.add(binding.circle2)
        circles.add(binding.circle3)
        circles.add(binding.circle4)
        circles.add(binding.circle5)

        // Set up TextWatchers for each EditText
        for (i in 0 until editTexts.size - 1) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        // Move focus to the next EditText
                        editTexts[i + 1].requestFocus()
                    }
                    else if (s?.length != 1 && i > 0) {
                        editTexts[i - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        // Set up a TextWatcher for the last EditText to stop when it's filled
        editTexts.last().addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) {
                    // Do something when the last EditText is filled
                    hideKeyboard()
                }
                else if (s?.length != 1) {
                    editTexts[editTexts.size - 2].requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        if (origWord == "") {
            origWord = gameViewModel.getRandomWordFromDictionary()
        }

        binding.btnCheckWord.setOnClickListener() {
            attempt++
            binding.tvAttempts.text = "Attempts: $attempt"

            for (editText in editTexts) {
                guessWord += editText.text
            }
            println(origWord)
            println(guessWord)

            val result = gameViewModel.checkWord(origWord, guessWord)
            for (idx in (0..result.count() - 1)) {
                if (result[idx] == 0) {
                    circles[idx].setImageResource(R.drawable.circle_shape_bad)
                } else if (result[idx] == 1) {
                    circles[idx].setImageResource(R.drawable.circle_shape_maybe)
                } else {
                    circles[idx].setImageResource(R.drawable.circle_shape_good)
                }
            }
            if (result.sum() == 10) {
                binding.btnCheckWord.isEnabled = false
                binding.btnCheckWord.isClickable = false
            }
            guessWord = ""
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}