package com.example.ccmoodle.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ccmoodle.adapters.CategoriesAdapter
import com.example.ccmoodle.adapters.CoursesAdapter
import com.example.ccmoodle.adapters.RegisteredCoursesAdapter
import com.example.ccmoodle.databinding.ActivityMainBinding
import com.example.ccmoodle.models.Category
import com.example.ccmoodle.models.Course
import com.example.ccmoodle.models.User
import com.example.ccmoodle.ui.CourseDetailsActivity.Companion.EXTRA_COURSE_ID
import com.example.ccmoodle.ui.CourseDetailsActivity.Companion.EXTRA_COURSE_NAME
import com.example.ccmoodle.utils.Helper
import com.example.ccmoodle.utils.Helper.Companion.toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() , CoursesAdapter.OnClick, RegisteredCoursesAdapter.OnClick, CategoriesAdapter.OnClick{
    val TAG = "MainActivity"
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser
    private lateinit var binding: ActivityMainBinding
    private lateinit var coursesAdapter: CoursesAdapter
    private lateinit var registeredCoursesAdapter: RegisteredCoursesAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coursesAdapter = CoursesAdapter(this, this)
        binding.rvCourses.adapter = coursesAdapter

        registeredCoursesAdapter = RegisteredCoursesAdapter(this, this)
        binding.rvRegisteredCourses.adapter = registeredCoursesAdapter

        categoriesAdapter = CategoriesAdapter(this, this)
        binding.rvCategories.adapter = categoriesAdapter
    }

    override fun onResume() {
        super.onResume()

        binding.etSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        initCategoriesAdapter()
        initCoursesAdapter()
        initRegisteredCoursesAdapter()
    }

    private fun initCategoriesAdapter() {
        db.collection(Category.CATEGORIES_COLLECTION).get().addOnSuccessListener { docs ->
            categoriesAdapter.setData(docs.toObjects(Category::class.java))
        }
    }

    private fun initCoursesAdapter() {
        db.collection(Course.COURSES_COLLECTION)
            .limit(20)
            .get()
            .addOnSuccessListener { docs ->
                val courses = docs.toObjects<Course>()
                coursesAdapter.setData(courses)
            }
            .addOnFailureListener {
                Helper.log(this, it.message!!)
                Helper.toast(this, "Error getting registered courses, ${it.message}")
            }
    }

    private fun initRegisteredCoursesAdapter() {
        db.collection(User.USERS_COLLECTION).document(user!!.uid).get()
            .addOnSuccessListener { doc ->
                if (doc[User.USER_ACTIVE_COURSES] == null) return@addOnSuccessListener

                val registeredCoursesIds = doc[User.USER_ACTIVE_COURSES] as ArrayList<*>
                val registeredCourses = ArrayList<Course>()

                registeredCoursesIds.forEach { courseId ->
                    Helper.log(this, courseId.toString())
                    db.collection(Course.COURSES_COLLECTION).document(courseId.toString()).get()
                        .addOnSuccessListener {
                            val img = it[Course.COURSE_IMG] as String
                            val title = it[Course.COURSE_TITLE] as String
                            val cat = it[Course.COURSE_CATEGORY] as String
                            val hours = it[Course.COURSE_HOURS] as Long
                            registeredCourses.add(Course(it.id, img, title, cat, hours.toInt()))

                            if (courseId.toString() == registeredCoursesIds.last().toString()) {
                                registeredCoursesAdapter.setData(registeredCourses)
                            }
                        }
                        .addOnFailureListener {
                            Helper.log(this, it.message!!)
                            Helper.toast(this, "Error getting registered courses, ${it.message}")
                        }
                }
                registeredCoursesAdapter.setData(registeredCourses)
            }
            .addOnFailureListener {
                Helper.log(this, it.message!!)
                Helper.toast(this, "Error getting registered courses, ${it.message}")
            }
    }

    override fun onClickCourse(course: Course) {
        val intent = Intent(this, CourseDetailsActivity::class.java)
        intent.putExtra(EXTRA_COURSE_ID, course.id)
        intent.putExtra(EXTRA_COURSE_NAME, course.title)
        startActivity(intent)
    }

    override fun onClickRegisteredCourse(course: Course) {
        val intent = Intent(this, CourseDetailsActivity::class.java)
        intent.putExtra(EXTRA_COURSE_ID, course.id)
        intent.putExtra(EXTRA_COURSE_NAME, course.title)
        startActivity(intent)
    }

    override fun onClickCategory(category: Category) {
        val i = Intent(applicationContext, SingleCategoryActivity::class.java)
        i.putExtra(SingleCategoryActivity.CATEGORY_NAME, category.name)
        startActivity(i)
    }
}