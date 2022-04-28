package com.example.ccmoodle.ui

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.ccmoodle.databinding.ActivityYtvideoBinding
import com.example.ccmoodle.models.Course
import com.example.ccmoodle.models.Lecture
import com.example.ccmoodle.utils.Helper
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions




class YTVideoActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityYtvideoBinding
    private val db = Firebase.firestore
    private var videoId: String? = null
    private var lectureId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityYtvideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoId = intent.getStringExtra(CourseDetailsActivity.LECTURE_VIDEO)!!
        val lectureId = intent.getStringExtra(CourseDetailsActivity.LECTURE_ID)!!
        val courseId = intent.getStringExtra(CourseDetailsActivity.COURSE_ID)!!

        Helper.toast(this, "We Prepare Video, Please Wait Some Time")

        val youTubePlayerView = binding.youtubePlayer
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.enterFullScreen();
        youTubePlayerView.enableAutomaticInitialization = false

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .rel(0)
            .build()

        youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
                db.collection(Course.COURSES_COLLECTION)
                    .document(courseId)
                    .collection(Lecture.LECTURES_COLLECTION)
                    .document(lectureId)
                    .update(Lecture.LECTURE_WATCHERS_IDS, FieldValue.arrayUnion(Helper.getCurrentUser()!!.uid))
            }
        }, true, iFramePlayerOptions)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayer.release()
    }
}