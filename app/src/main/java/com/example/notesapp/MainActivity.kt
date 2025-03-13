package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val notes = mutableListOf<Note>() // Stores notes in memory
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewNotes)
        val fabAddNote: FloatingActionButton = findViewById(R.id.fabAddNote)

        adapter = NoteAdapter(notes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Load saved notes from SharedPreferences
        loadNotes()

        // Open NoteEditorActivity when clicking the "+" button
        fabAddNote.setOnClickListener {
            val intent = Intent(this, NoteEditorActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            val title = data?.getStringExtra("title") ?: ""
            val content = data?.getStringExtra("content") ?: ""

            if (title.isNotEmpty() || content.isNotEmpty()) {
                notes.add(Note(title, content))
                saveNotes()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun saveNotes() {
        val sharedPreferences = getSharedPreferences("NotesApp", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val notesString = notes.joinToString("||") { "${it.title}::${it.content}" }
        editor.putString("notes", notesString)
        editor.apply()
    }

    private fun loadNotes() {
        val sharedPreferences = getSharedPreferences("NotesApp", MODE_PRIVATE)
        val notesString = sharedPreferences.getString("notes", "") ?: ""

        if (notesString.isNotEmpty()) {
            notes.clear()
            notesString.split("||").forEach {
                val parts = it.split("::")
                if (parts.size == 2) {
                    notes.add(Note(parts[0], parts[1]))
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_NOTE = 100
    }
}
