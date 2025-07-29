package com.saurabh.mynotes

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.saurabh.mynotes.database.NoteDatabase
import com.saurabh.mynotes.database.NoteDatabase.Companion.invoke
import com.saurabh.mynotes.fragment.EditNoteFragment
import com.saurabh.mynotes.model.Note
import com.saurabh.mynotes.repository.NoteRepository
import com.saurabh.mynotes.viewmodel.NoteViewModel
import com.saurabh.mynotes.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#19375d")))


        setupViewModel()

        val note = intent.getParcelableExtra<Note>("note")
        val source = intent.getStringExtra("source")

        if (source == "favorite" || note != null) {
            val fragment = EditNoteFragment()
            val bundle = Bundle().apply {
                putParcelable("note", note)
                putString("source", "favorite")
            }
            fragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
        }
    }

    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }
}