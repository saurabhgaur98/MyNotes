package com.saurabh.mynotes

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.saurabh.mynotes.adapter.FavNoteAdapter
import com.saurabh.mynotes.database.NoteDatabase
import com.saurabh.mynotes.model.Note
import com.saurabh.mynotes.repository.NoteRepository
import com.saurabh.mynotes.viewmodel.NoteViewModel
import com.saurabh.mynotes.viewmodel.NoteViewModelFactory
import com.saurabh.mynotes.databinding.ActivityFavoriteNotesBinding

class FavoriteNotes : AppCompatActivity(){

    private var _binding: ActivityFavoriteNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favNoteAdapter: FavNoteAdapter
    private lateinit var notesViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#19375d")))
        supportActionBar?.title = "Favorite Notes"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)

        // Initialize ViewModel
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        notesViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]

        setupRecyclerView()
        observeFavoriteNotes()
    }

    private fun setupRecyclerView() {
        favNoteAdapter = FavNoteAdapter { note ->
            handleFavoriteClick(note) // Handle the favorite button click
        }

        binding.favRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = favNoteAdapter
        }
    }

    private fun observeFavoriteNotes() {
        notesViewModel.getFavoriteNotes().observe(this) { favoriteNotes ->
            if (favoriteNotes != null) {
                favNoteAdapter.setNotes(favoriteNotes) //defined in he adapter, Update the adapter with the new favorite notes
                updateUI(favoriteNotes)
            }
        }
    }

    private fun updateUI(noteList: List<Note>) {
        if (noteList.isNotEmpty()) {
            binding.emptyNotesText.visibility = View.GONE
            binding.favRecyclerView.visibility = View.VISIBLE
        } else {
            binding.emptyNotesText.visibility = View.VISIBLE
            binding.favRecyclerView.visibility = View.GONE
        }
    }

    private fun handleFavoriteClick(note: Note) {
        note.isFavorite = false // Set the note as not favorite
        notesViewModel.updateNote(note) // Update the note in the database

        Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
