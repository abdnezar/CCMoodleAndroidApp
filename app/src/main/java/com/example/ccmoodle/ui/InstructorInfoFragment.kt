package com.example.ccmoodle.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ccmoodle.R
import com.example.ccmoodle.databinding.FragmentIntractorInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InstructorInfoFragment : BottomSheetDialogFragment() {
    private val TAG = this.javaClass.simpleName
    private var _binding : FragmentIntractorInfoBinding? = null
    private lateinit var binding : FragmentIntractorInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentIntractorInfoBinding.inflate(inflater, container, false)
        binding = _binding!!





        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}