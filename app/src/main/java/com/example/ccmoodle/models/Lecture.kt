package com.example.ccmoodle.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

class Lecture {
    @Exclude private var id: String = ""
    var title : String = ""
    var docsUrl : String = ""
    var vUrl : String = ""
    var date : Timestamp = Timestamp.now()
    var watchersIds  = listOf<String>()

    constructor()
    constructor(id: String, title: String, docsUrl: String, vUrl: String, watchersIds: List<String>, date: Timestamp = Timestamp.now()) {
        this.id = id
        this.title = title
        this.docsUrl = docsUrl
        this.vUrl = vUrl
        this.watchersIds = watchersIds
        this.date = date
    }

    fun getId() : String {
        return id
    }

    companion object {
        const val LECTURES_COLLECTION = "Lectures"

        const val LECTURE_ID = "id"
        const val LECTURE_TITLE = "title"
        const val LECTURE_VIDEO_URL = "vUrl"
        const val LECTURE_DOCS_URL = "docsUrl"
        const val LECTURE_WATCHERS_IDS = "watchersIds"
        const val LECTURE_Date = "date"
    }
}