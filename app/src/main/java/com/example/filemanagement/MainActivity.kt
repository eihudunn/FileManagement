package com.example.filemanagement

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import com.example.filemanagement.R
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter
    private var currentPath: String =  Environment.getExternalStorageDirectory().absolutePath

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Hiển thị nút back trong ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(currentPath)

        loadFiles(currentPath)

        // Xử lý khi nhấn nút Back
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        })
    }

    private fun loadFiles(path: String) {
        val directory = File(path)
        if (directory.exists() && directory.isDirectory) {
            currentPath = directory.path
            supportActionBar?.title = currentPath // Update title

            val files = directory.listFiles()?.toList() ?: emptyList()
            for (entry in directory.listFiles()) {
                if (entry.isDirectory)
                    Log.v("TAG", "Directory")
                else if (entry.isFile)
                    Log.v("TAG", "File")
                Log.v("TAG", entry.path)
            }
            adapter = FileAdapter(files) { file ->
                if (file.isDirectory) {
                    loadFiles(file.path)
                } else {
                    if (file.extension == "txt") {
                        val intent = Intent(this, FileContentActivity::class.java)
                        intent.putExtra("filePath", file.path)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Cannot open this file type", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            recyclerView.adapter = adapter
        } else {
            Toast.makeText(this, "Invalid directory!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateBack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateBack() {
        val parentFile = File(currentPath).parentFile
        if (parentFile != null && parentFile.exists()) {
            loadFiles(parentFile.path)
        } else {
            Toast.makeText(this, "Bạn đang ở thư mục gốc", Toast.LENGTH_SHORT).show()
        }
    }
}