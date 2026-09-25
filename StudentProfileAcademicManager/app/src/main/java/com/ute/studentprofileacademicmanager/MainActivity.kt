package com.ute.studentprofileacademicmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ute.studentprofileacademicmanager.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val defaultStudent = Student("2415053122248", "Pham Tran Thanh Vinh", "24T2", "vinh.nv@ute.udn.vn", 3.75)
    private var currentStudent = defaultStudent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khôi phục nếu vừa xoay màn hình
        @Suppress("DEPRECATION")
        savedInstanceState?.getSerializable("KEY_STUDENT")?.let {
            currentStudent = it as Student
        }

        bindStudentData(currentStudent)

        // Bắt sự kiện cập nhật điểm
        binding.btnUpdateGpa.setOnClickListener {
            val gpaText = binding.edtGpaInput.text.toString().trim()
            val gpa = gpaText.toDoubleOrNull()

            if (gpa == null || gpa !in 0.0..4.0) {
                binding.edtGpaInput.error = "GPA phải từ 0.0 đến 4.0"
                return@setOnClickListener
            }

            currentStudent = currentStudent.copy(gpa = gpa)
            bindStudentData(currentStudent)
            Toast.makeText(this, "Đã cập nhật GPA thành công!", Toast.LENGTH_SHORT).show()
        }
    }

    // Cần có hàm này để gán dữ liệu lên giao diện, tránh lỗi "Unresolved reference 'bindStudentData'"
    private fun bindStudentData(student: Student) {
        // Ví dụ: binding.tvStudentName.text = student.name
    }

    // Cần lưu lại trạng thái để tránh lỗi khi xoay màn hình
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("KEY_STUDENT", currentStudent as Serializable)
    }
}