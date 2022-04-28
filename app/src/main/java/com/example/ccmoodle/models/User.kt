package com.example.ccmoodle.models

import com.google.firebase.firestore.Exclude

class User {
    @Exclude private var id: String = ""
    var firstName: String = ""
    var middleName: String = ""
    var lastName: String = ""
    var birthdayYear: Int = 1900
    var address: String = ""
    var phone: Long = 0L
    var email: String = ""
    var role: Int = 0
    var token: String = ""
    var activeCourses : List<String>? = listOf()
    var finishedCourses : List<String>? = listOf()

    constructor()
    constructor(id: String, firstName: String, middleName: String, lastName: String, birthdayYear: Int, address: String, phone: Long, email: String, role: Int, token: String, activeCourses: List<String>?, finishedCourses: List<String>?) {
        this.id = id
        this.firstName = firstName
        this.middleName = middleName
        this.lastName = lastName
        this.birthdayYear = birthdayYear
        this.address = address
        this.phone = phone
        this.email = email
        this.role = role
        this.token = token
        this.activeCourses = activeCourses
        this.finishedCourses = finishedCourses
    }

    companion object {
        const val USERS_COLLECTION = "USERS"

        const val STUDENT_ENUM = 1
        const val TEACHER_ENUM = 2

        const val SHARED_PREF_NAME = "user"

        const val USER_ID = "id"
        const val USER_FIRST_NAME = "firstName"
        const val USER_MIDDLE_NAME = "middleName"
        const val USER_FAMILY_NAME = "lastName"
        const val USER_BIRTHDAY = "birthdayYear"
        const val USER_ADDRESS = "address"
        const val USER_PHONE = "phone"
        const val USER_EMAIL = "email"
        const val USER_ROLE = "role"
        const val USER_TOKEN = "token"
        const val USER_ACTIVE_COURSES = "activeCourses"
        const val USER_FINISHED_COURSES = "finishedCourses"
    }
}