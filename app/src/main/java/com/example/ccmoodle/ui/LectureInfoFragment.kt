package com.example.ccmoodle.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.ccmoodle.databinding.FragmentLectureInfoBinding
import com.example.ccmoodle.models.Assignment
import com.example.ccmoodle.models.Course
import com.example.ccmoodle.models.Lecture
import com.example.ccmoodle.utils.Helper
import com.example.ccmoodle.utils.Helper.Companion.log
import com.example.ccmoodle.utils.Helper.Companion.openUrl
import com.example.ccmoodle.utils.Helper.Companion.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream

class LectureInfoFragment : BottomSheetDialogFragment() {
    private val TAG = this.javaClass.simpleName
    private var db = Firebase.firestore
    private var storage = Firebase.storage
    private var _binding : FragmentLectureInfoBinding? = null
    private lateinit var binding : FragmentLectureInfoBinding
    private var lectureId : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLectureInfoBinding.inflate(inflater, container, false)
        binding = _binding!!

        lectureId = arguments?.getString(CourseDetailsActivity.LECTURE_ID)
        val lectureVideoUrl = arguments?.getString(CourseDetailsActivity.LECTURE_VIDEO)
        val lectureDocsUrl = arguments?.getString(CourseDetailsActivity.LECTURE_DOCS)
        val courseId = arguments?.getString(CourseDetailsActivity.COURSE_ID)

        binding.btnWatch.setOnClickListener {
            val i = Intent(activity, YTVideoActivity::class.java)
            i.putExtra(CourseDetailsActivity.LECTURE_VIDEO, lectureVideoUrl)
            i.putExtra(CourseDetailsActivity.LECTURE_ID, lectureId)
            i.putExtra(CourseDetailsActivity.COURSE_ID, courseId)
            activity?.startActivity(i)
        }

        binding.btnSeeDocs.setOnClickListener {
            if (lectureDocsUrl != null) {
                openUrl(requireActivity(), lectureDocsUrl)
            } else {
                toast(requireActivity(), "No documents available")
            }
        }

        binding.btnSubmitAssignment.setOnClickListener {
            if (binding.etLectureAssignment.text.isNotEmpty() && Patterns.WEB_URL.matcher(binding.etLectureAssignment.text.toString()).matches()) {
                db.collection(Course.COURSES_COLLECTION)
                    .document(courseId!!)
                    .collection(Lecture.LECTURES_COLLECTION)
                    .document(lectureId!!)
                    .collection(Assignment.COLLECTION_NAME)
                    .add(Assignment("", binding.etLectureAssignment.text.toString(), Helper.getCurrentUser()!!.uid, Timestamp.now()))
                    .addOnSuccessListener {
                        toast(requireActivity(), "Assignment submitted successfully")
                        this.dismiss()
                    }
                    .addOnFailureListener {
                        toast(requireActivity(), "Assignment submission failed, ${it.message}")
                    }
            } else {
                toast(requireActivity(), "Please enter your assignment link, should be a valid URL")
            }
        }

        return binding.root
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}