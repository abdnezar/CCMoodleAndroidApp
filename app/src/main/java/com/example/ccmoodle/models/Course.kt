package com.example.ccmoodle.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

class Course {
    @Exclude private var id: String = ""
    var img: String = ""
    var title: String = ""
    var category: String = ""
    var registersIds = listOf<String>()
    var desc: String = ""
    var ownerId: String = ""
    var hours: Int = 0
    var createDate: Timestamp = Timestamp.now()
    var lastUpdateDate: Timestamp = Timestamp.now()

    // Firestore requires a no-arg constructor
    constructor()

    // constructor for creating a new course
    constructor(id: String, img: String, title: String, category: String, registersIds: List<String>, desc: String, ownerId: String, hours: Int) {
        this.id = id
        this.img = img
        this.title = title
        this.category = category
        this.registersIds = registersIds
        this.desc = desc
        this.ownerId = ownerId
        this.hours = hours
    }

    // Most Sell Adapter Constructor
    constructor(id: String,img: String, title: String, category: String, hours: Int) {
        this.id = id
        this.img = img
        this.title = title
        this.category = category
        this.hours = hours
    }

    fun getId(): String {
        return id
    }

    companion object {
        const val COURSES_COLLECTION = "COURSES"

        const val COURSE_ID = "id"
        const val COURSE_IMG = "img"
        const val COURSE_TITLE = "title"
        const val COURSE_CATEGORY = "category"
        const val COURSE_DESC = "desc"
        const val COURSE_OWNER_ID = "ownerId"
        const val COURSE_HOURS = "hours"
        const val COURSE_REGISTERS_IDS = "registersIds"
    }
}