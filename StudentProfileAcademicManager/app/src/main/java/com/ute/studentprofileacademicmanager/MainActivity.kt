package com.ute.studentprofileacademicmanager

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import Model.Student
import com.ute.studentprofileacademicmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Cập nhật chuỗi tên chính xác
    private val defaultStudent = Student(
        studentId = "2415053122248",
        name = "PHAM TRAN THANH VINH",
        className = "24T2",
        email = "Vinh.nv@ute.udn.vn",
        gpa = 3.75
    )
    private var currentStudent = defaultStudent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        savedInstanceState?.getSerializable("KEY_STUDENT")?.let {
            currentStudent = it as Student
        }

        bindStudentData(currentStudent)

        binding.btnUpdateGpa.setOnClickListener {
            val gpaText = binding.edtGpaInput.text.toString().trim()
            val gpa = gpaText.toDoubleOrNull()

            if (gpa == null || gpa !in 0.0..4.0) {
                binding.edtGpaInput.error = "GPA phải từ 0.0 đến 4.0"
                return@setOnClickListener
            }

            currentStudent = currentStudent.copy(gpa = gpa)
            bindStudentData(currentStudent)

            binding.edtGpaInput.text?.clear()
            binding.edtGpaInput.error = null

            Toast.makeText(this, "Đã cập nhật GPA thành công!", Toast.LENGTH_SHORT).show()
        }

        // --- CẬP NHẬT MỞ RỘNG 2 Ở ĐÂY ---
        binding.btnReset.setOnClickListener {
            // Sử dụng Scope function 'apply' để thiết lập AlertDialog
            AlertDialog.Builder(this).apply {
                setTitle("Xác nhận khôi phục")
                setMessage("Bạn có chắc chắn muốn đặt lại điểm GPA ban đầu (${defaultStudent.gpa}) không?")

                // Nút Hủy: Đóng hộp thoại
                setNegativeButton("Hủy") { dialog, _ ->
                    dialog.dismiss()
                }

                // Nút Đồng ý: Thực hiện logic reset
                setPositiveButton("Đồng ý") { _, _ ->
                    currentStudent = defaultStudent
                    bindStudentData(currentStudent)
                    binding.edtGpaInput.text?.clear()
                    binding.edtGpaInput.error = null

                    Toast.makeText(this@MainActivity, "Đã khôi phục dữ liệu gốc!", Toast.LENGTH_SHORT).show()
                }
            }.show() // Gọi show() để hiển thị Dialog
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindStudentData(student: Student) {
        binding.tvStudentName.text = student.name
        binding.tvStudentId.text = "MSSV: ${student.studentId}"

        val rank = when {
            student.gpa >= 3.6 -> "Xuất sắc"
            student.gpa >= 3.2 -> "Giỏi"
            student.gpa >= 2.5 -> "Khá"
            student.gpa >= 2.0 -> "Trung bình"
            else -> "Yếu"
        }

        binding.tvGpaBadge.text = "${student.gpa} GPA ($rank)"
        binding.tvGpaBadge.setBackgroundColor(student.gpa.toRankingColor())
        binding.tvGpaBadge.setTextColor(Color.WHITE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("KEY_STUDENT", currentStudent)
    }

    private fun Double.toRankingColor(): Int {
        return when {
            this >= 3.6 -> "#34B469".toColorInt()
            this >= 3.2 -> "#00BCD4".toColorInt()
            this >= 2.5 -> "#FF9800".toColorInt()
            else -> "#F44336".toColorInt()
        }
    }
}