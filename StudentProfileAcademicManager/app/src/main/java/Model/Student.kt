package Model

import java.io.Serializable

data class Student(
    val studentId: String,
    val name: String,
    val className: String,
    val email: String,
    val gpa: Double
) : Serializable