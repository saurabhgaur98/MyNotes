package com.saurabh.mynotes.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saurabh.mynotes.MainActivity
import com.saurabh.mynotes.model.Note
import com.saurabh.mynotes.R
import com.saurabh.mynotes.databinding.NoteLayoutBinding

class FavNoteAdapter(
    private val onFavoriteClick: (Note) -> Unit // Callback for handling favorite button clicks
) : RecyclerView.Adapter<FavNoteAdapter.FavViewHolder>() {

    private var favNotes: MutableList<Note> = mutableListOf() // Use a mutable list for efficient updates

    class FavViewHolder(val itemBinding: NoteLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val currentNote = favNotes[position]

        holder.itemBinding.apply {
            // Bind note data to the UI
            noteTitle.text = currentNote.noteTitle
            noteDesc.text = currentNote.noteDesc
            addFavBtn.setImageResource(R.drawable.favorite_icon)

            // Handle the favorite button click
            addFavBtn.setOnClickListener {
                currentNote.isFavorite = false // Set the favorite status to false
                onFavoriteClick(currentNote) // Notify the parent about the change
            }
        }

        //to navigate from favorite notes to edit note
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MainActivity::class.java)
            intent.putExtra("note", currentNote)
            intent.putExtra("source", "favorite")
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = favNotes.size

     //Updates the adapter with a new list of favorite notes.
    @SuppressLint("NotifyDataSetChanged")
    fun setNotes(newNotes: List<Note>) {
        favNotes = newNotes.toMutableList() // Replace the current list with a new one
        notifyDataSetChanged() // Notify RecyclerView to refresh
    }

}
