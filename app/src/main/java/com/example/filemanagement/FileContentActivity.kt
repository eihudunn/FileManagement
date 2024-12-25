package com.example.filemanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.filemanagement.R
import java.io.File

class FileContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_content)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val filePath = intent.getStringExtra("filePath")
        val textView: TextView = findViewById(R.id.textView)

        if (filePath != null) {
            val file = File(filePath)
            val fileName = file.name;
            supportActionBar?.setTitle(fileName)
            if (file.exists() && file.isFile) {
                val content = file.readText()
                textView.text = content
            } else {
                textView.text = "Không thể đọc tệp!"
            }
        }
    }
}
