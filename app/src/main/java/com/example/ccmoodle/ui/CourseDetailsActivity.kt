package com.example.ccmoodle.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ccmoodle.adapters.CoursesAdapter
import com.example.ccmoodle.adapters.LecturesAdapter
import com.example.ccmoodle.databinding.ActivityCourseDetailsBinding
import com.example.ccmoodle.models.Course
import com.example.ccmoodle.models.Course.Companion.COURSE_ID
import com.example.ccmoodle.models.Lecture
import com.example.ccmoodle.models.User
import com.example.ccmoodle.utils.Helper
import com.example.ccmoodle.utils.Helper.Companion.fillImage
import com.example.ccmoodle.utils.Helper.Companion.formatDate
import com.example.ccmoodle.utils.Helper.Companion.log
import com.example.ccmoodle.utils.Helper.Companion.toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CourseDetailsActivity : AppCompatActivity(), LecturesAdapter.OnClick {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCourseDetailsBinding
    private lateinit var lecturesAdapter: LecturesAdapter
    private var db = Firebase.firestore
    private var user = Firebase.auth.currentUser
    private var courseId : String = ""
    private var isCourseRegistered : Boolean = false

    companion object {
        const val LECTURE_VIDEO = "lectureVideo"
        const val LECTURE_DOCS = "lectureDocs"
        const val LECTURE_ID = "lectureId"
        const val COURSE_ID = "courseId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lecturesAdapter = LecturesAdapter(this, this)
        binding.rvLectures.adapter = lecturesAdapter

        courseId = intent.getStringExtra(MainActivity.EXTRA_COURSE_ID)!!
        isCourseRegistered = intent.getBooleanExtra(MainActivity.EXTRA_IS_COURSE_REGISTERED, false)

        if (isCourseRegistered) {
            binding.btnRegisterCourse.text = "Unregister Course"
        } else {
            binding.btnRegisterCourse.text = "Register Course"
        }

        getCourseInfo()
        getLectures()
    }

    private fun getLectures() {
        val lectures = ArrayList<Lecture>()

        db.collection(Course.COURSES_COLLECTION)
            .document(courseId)
            .collection(Lecture.LECTURES_COLLECTION)
            .orderBy(Lecture.LECTURE_Date)
            .get()
            .addOnSuccessListener { docs ->
                docs.documents.forEach {
                    val title = it[Lecture.LECTURE_TITLE] as String
                    val docsUrl = it[Lecture.LECTURE_DOCS_URL] as String
                    val videoUrl = it[Lecture.LECTURE_VIDEO_URL] as String
                    val watchersIds = it[Lecture.LECTURE_WATCHERS_IDS] as List<String>
                    lectures.add(Lecture(it.id, title, docsUrl, videoUrl, watchersIds))
                }
                lecturesAdapter.setData(lectures)
            }
            .addOnFailureListener {
                log(this, it.message!!)
                toast(applicationContext, "Error getting registered courses, ${it.message}")
            }
    }

    override fun onResume() {
        super.onResume()

        binding.cvInstructor.setOnClickListener {
            InstructorInfoFragment().show(supportFragmentManager, "InstructorInfoFragment")
        }

        binding.btnRegisterCourse.setOnClickListener {
            if (isCourseRegistered) {
                unregisterCourse()
            } else {
                checkIsCanAddCourse()
            }
        }
    }

    private fun unregisterCourse() {
        db.collection(User.USERS_COLLECTION)
            .document(user!!.uid)
            .update(User.USER_ACTIVE_COURSES, FieldValue.arrayRemove(courseId), User.USER_FINISHED_COURSES, FieldValue.arrayUnion(courseId))
            .addOnSuccessListener {
                toast(this, "Course Unregistered Successfully")
                isCourseRegistered = false
                binding.btnRegisterCourse.text = "Register Course"
            }
            .addOnFailureListener {
                log(this, "Error removing document: ${it.message}")
                toast(this, "Course Unregistered Failed, ${it.message}")
            }
    }

    private fun checkIsCanAddCourse() {
        db.collection(User.USERS_COLLECTION)
            .document(user!!.uid)
            .get()
            .addOnSuccessListener {
                val activeCourses = it[User.USER_ACTIVE_COURSES] as List<String>
                if (activeCourses.size < 5) {
                    registerCourse()
                }
            }
            .addOnFailureListener {
                log(this, "Error removing document: ${it.message}")
                toast(this, "Course Registered Failed, ${it.message}")
            }
    }

    private fun registerCourse(){
        db.collection(User.USERS_COLLECTION)
            .document(user!!.uid)
            .update(User.USER_ACTIVE_COURSES, FieldValue.arrayUnion(courseId))
            .addOnSuccessListener {
                toast(this, "Course Registered Successfully")
                isCourseRegistered = true
                binding.btnRegisterCourse.text = "Unregister Course"
            }
            .addOnFailureListener {
                toast(this, "Course Registered Failed, ${it.message}")
            }
    }

    private fun getCourseInfo() {
        db.collection(Course.COURSES_COLLECTION)
            .document(courseId)
            .get()
            .addOnSuccessListener {
                it.toObject(Course::class.java)?.let { course ->
                    fillImage(this, course.img, binding.ivCourse)
                    binding.tvCourseName.text = course.title
                    binding.tvCourseDesc.text = course.desc
                    binding.tvCourseCategory.text = course.category
                    binding.tvCourseHours.text = "${course.hours} \n hours"
                    binding.tvCourseRegistration.text = "${course.registersIds.size} \n registration"
                    binding.tvCourseCreateDate.text = "${binding.tvCourseCreateDate.text} ${formatDate(course.createDate)}"
                    binding.tvCourseLastUpdate.text = "${binding.tvCourseLastUpdate.text} ${formatDate(course.lastUpdateDate)}"
                    binding.tvCourseDesc.text = course.desc

                    fillInstructorInfo(course.ownerId)
                }
            }.addOnFailureListener {
                log(this, "Error getting course info $it")
                toast(applicationContext, "Error getting course info $it")
                finish()
            }
    }

    private fun fillInstructorInfo(ownerId: String) {
        db.collection(User.USERS_COLLECTION).document(ownerId).get()
            .addOnSuccessListener {
                it.toObject(User::class.java)?.let { user ->
                    binding.tvInstructorName.text ="${user.firstName} ${user.middleName[0].uppercaseChar()} ${user.lastName}"
                    binding.tvInstructorEmail.text = user.email
                }
            }
            .addOnFailureListener {
                log(this, "Error getting instructor info $it")
                toast(applicationContext, "Error getting instructor info $it")
            }
    }

    override fun onClickLecture(lecture: Lecture) {
        if (isCourseRegistered) {
            LectureInfoFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(LECTURE_VIDEO, lecture.vUrl)
                        putString(LECTURE_DOCS, lecture.docsUrl)
                        putString(LECTURE_ID, lecture.getId())
                        putString(COURSE_ID, courseId)
                    }
                }
                .show(supportFragmentManager, "LectureInfoFragment")
        } else {
            toast(this, "You must register course to view lectures")
        }
    }

}