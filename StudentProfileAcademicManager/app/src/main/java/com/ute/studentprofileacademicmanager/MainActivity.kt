package com.ute.studentprofileacademicmanager

import Model.Student
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.ute.studentprofileacademicmanager.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val defaultStudent = Student(
        studentId = "2415053122248",
        name = "PHAM TRAN THANH VINH",
        className = "24T2",
        email = "vinh.nv@ute.udn.vn",
        gpa = 3.75
    )
    private var currentStudent = defaultStudent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khôi phục trạng thái khi xoay màn hình
        @Suppress("DEPRECATION")
        savedInstanceState?.getSerializable("KEY_STUDENT")?.let {
            currentStudent = it as Student
        }

        bindStudentData(currentStudent)

        // 1. Nút Cập nhật GPA
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

        // 2. Nút Khôi phục mặc định
        binding.btnReset.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Xác nhận khôi phục")
                setMessage("Bạn có chắc chắn muốn đặt lại điểm GPA ban đầu (${defaultStudent.gpa}) không?")
                setNegativeButton("Hủy") { dialog, _ -> dialog.dismiss() }
                setPositiveButton("Đồng ý") { _, _ ->
                    currentStudent = defaultStudent
                    bindStudentData(currentStudent)
                    binding.edtGpaInput.text?.clear()
                    binding.edtGpaInput.error = null
                    Toast.makeText(this@MainActivity, "Đã khôi phục dữ liệu gốc!", Toast.LENGTH_SHORT).show()
                }
            }.show()
        }

        // 3. Nút Gửi Báo Cáo Học Tập (Tự động điền đầy đủ Email, Tiêu đề và Nội dung)
        binding.btnSendReport.setOnClickListener {
            val rank = getRank(currentStudent.gpa)
            val emailSubject = "[Báo cáo học tập] Sinh viên ${currentStudent.name} - MSSV ${currentStudent.studentId}"
            val emailBody = "Kính gửi thầy/cô,\n\n" +
                    "Em xin báo cáo kết quả học tập hiện tại như sau:\n" +
                    "- Họ và tên: ${currentStudent.name}\n" +
                    "- MSSV: ${currentStudent.studentId}\n" +
                    "- Lớp: ${currentStudent.className}\n" +
                    "- Điểm GPA: ${currentStudent.gpa}\n" +
                    "- Xếp loại học lực: $rank\n\n" +
                    "Trân trọng,\n" +
                    currentStudent.name

            // Sử dụng ACTION_SEND kết hợp định dạng message/rfc822 để Gmail nhận đủ Subject và Body
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(currentStudent.email))
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                putExtra(Intent.EXTRA_TEXT, emailBody)
            }

            try {
                startActivity(Intent.createChooser(intent, "Gửi báo cáo qua..."))
            } catch (_: Exception) {
                Toast.makeText(this, "Không tìm thấy ứng dụng Email nào trên máy!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Hàm hiển thị dữ liệu lên giao diện
    @SuppressLint("SetTextI18n")
    private fun bindStudentData(student: Student) {
        binding.tvStudentName.text = student.name
        binding.tvStudentId.text = "MSSV: ${student.studentId}"

        val rank = getRank(student.gpa)

        binding.tvGpaBadge.text = "${student.gpa} GPA ($rank)"
        binding.tvGpaBadge.setBackgroundColor(student.gpa.toRankingColor())
        binding.tvGpaBadge.setTextColor(Color.WHITE)
    }

    // Hàm tính xếp loại học lực dựa trên GPA
    private fun getRank(gpa: Double): String {
        return when {
            gpa >= 3.6 -> "Xuất sắc"
            gpa >= 3.2 -> "Giỏi"
            gpa >= 2.5 -> "Khá"
            gpa >= 2.0 -> "Trung bình"
            else -> "Yếu"
        }
    }

    // Lưu lại trạng thái khi xoay ngang/dọc màn hình
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("KEY_STUDENT", currentStudent)
    }

    // Hàm mở rộng tạo màu sắc tương ứng với mức điểm
    private fun Double.toRankingColor(): Int {
        return when {
            this >= 3.6 -> "#34B469".toColorInt()
            this >= 3.2 -> "#00BCD4".toColorInt()
            this >= 2.5 -> "#FF9800".toColorInt()
            else -> "#F44336".toColorInt()
        }
    }
}