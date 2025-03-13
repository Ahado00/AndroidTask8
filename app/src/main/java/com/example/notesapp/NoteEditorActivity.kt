package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NoteEditorActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var tvCharCount: TextView
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_editor)

        etTitle = findViewById(R.id.etNoteTitle)
        etContent = findViewById(R.id.etNoteContent)
        tvCharCount = findViewById(R.id.tvCharCount)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // Update character count as user types
        etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tvCharCount.text = "${s?.length ?: 0}/500"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Save note and return to MainActivity
        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            val resultIntent = Intent()
            resultIntent.putExtra("title", title)
            resultIntent.putExtra("content", content)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // Cancel button to go back
        btnCancel.setOnClickListener {
            finish()
        }
    }
}
