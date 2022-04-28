package com.example.ccmoodle.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

class Assignment {
    @Exclude private var id : String = ""
    var date: Timestamp = Timestamp.now()
    var fileUrl: String = ""
    var userId: String = ""

    constructor()
    constructor(id: String, fileUrl: String, userId: String,  date: Timestamp = Timestamp.now()) {
        this.id = id
        this.date = date
        this.fileUrl = fileUrl
        this.userId = userId
    }

    fun getId(): String {
        return id
    }

    companion object {
        const val COLLECTION_NAME = "Assignments"

        const val ASSIGNMENT_ID = "id"
        const val ASSIGNMENT_DATE = "date"
        const val ASSIGNMENT_FILE_URL = "fileUrl"
        const val ASSIGNMENT_USER_ID = "userId"
    }
}