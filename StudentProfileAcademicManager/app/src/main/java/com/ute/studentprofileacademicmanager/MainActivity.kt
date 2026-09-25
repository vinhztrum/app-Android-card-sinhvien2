package com.ute.studentprofileacademicmanager

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import Model.Student
import com.ute.studentprofileacademicmanager.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Khởi tạo sinh viên mặc định
    private val defaultStudent = Student(
        studentId = "2415053122248",
        name = "Phạm Trần Thanh Vinh",
        className = "24T2",
        email = "Vinh.nv@ute.udn.vn",
        gpa = 3.75
    )
    private var currentStudent = defaultStudent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khôi phục dữ liệu nếu thiết bị vừa xoay màn hình
        @Suppress("DEPRECATION")
        savedInstanceState?.getSerializable("KEY_STUDENT")?.let {
            currentStudent = it as Student
        }

        // Hiển thị dữ liệu lên giao diện
        bindStudentData(currentStudent)

        // Bắt sự kiện nút Cập nhật GPA
        binding.btnUpdateGpa.setOnClickListener {
            val gpaText = binding.edtGpaInput.text.toString().trim()
            val gpa = gpaText.toDoubleOrNull()

            // Kiểm tra tính hợp lệ
            if (gpa == null || gpa !in 0.0..4.0) {
                binding.edtGpaInput.error = "GPA phải từ 0.0 đến 4.0"
                return@setOnClickListener
            }

            // Cập nhật điểm và hiển thị lại
            currentStudent = currentStudent.copy(gpa = gpa)
            bindStudentData(currentStudent)

            // Xoá nội dung ô nhập sau khi cập nhật thành công
            binding.edtGpaInput.text?.clear()
            binding.edtGpaInput.error = null

            Toast.makeText(this, "Đã cập nhật GPA thành công!", Toast.LENGTH_SHORT).show()
        }

        // Bắt sự kiện nút Khôi phục mặc định
        binding.btnReset.setOnClickListener {
            currentStudent = defaultStudent
            bindStudentData(currentStudent)
            binding.edtGpaInput.text?.clear()
            binding.edtGpaInput.error = null
            Toast.makeText(this, "Đã khôi phục dữ liệu gốc!", Toast.LENGTH_SHORT).show()
        }
    }

    // Thêm @SuppressLint để ẩn cảnh báo ghép chuỗi Hardcoded trong setText
    @SuppressLint("SetTextI18n")
    private fun bindStudentData(student: Student) {
        binding.tvStudentName.text = student.name
        binding.tvStudentId.text = "MSSV: ${student.studentId}"

        // Logic phân loại xếp loại dựa trên điểm số GPA
        val rank = when {
            student.gpa >= 3.6 -> "Xuất sắc"
            student.gpa >= 3.2 -> "Giỏi"
            student.gpa >= 2.5 -> "Khá"
            student.gpa >= 2.0 -> "Trung bình"
            else -> "Yếu"
        }
        // Lệnh when đã kết thúc tại đây

        // Đưa các lệnh gọi binding ra BÊN NGOÀI khối when
        binding.tvGpaBadge.text = "${student.gpa} GPA ($rank)"
        binding.tvGpaBadge.setBackgroundColor(student.gpa.toRankingColor())
        binding.tvGpaBadge.setTextColor(Color.WHITE)
    }

    // Lưu trạng thái trước khi cấu hình thay đổi (ví dụ: xoay ngang màn hình)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("KEY_STUDENT", currentStudent)
    }

    // Gợi ý của Android Studio: Dùng .toColorInt() thay cho Color.parseColor()
    private fun Double.toRankingColor(): Int {
        return when {
            this >= 3.6 -> "#34B469".toColorInt() // Xuất sắc (Xanh lá)
            this >= 3.2 -> "#00BCD4".toColorInt() // Giỏi (Xanh Cyan)
            this >= 2.5 -> "#FF9800".toColorInt() // Khá (Cam Amber)
            else -> "#F44336".toColorInt()        // Trung bình / Yếu (Đỏ)
        }
    }
}