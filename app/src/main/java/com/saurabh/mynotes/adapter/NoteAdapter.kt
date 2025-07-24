package com.saurabh.mynotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saurabh.mynotes.fragment.HomeFragmentDirections
import com.saurabh.mynotes.model.Note
import com.saurabh.mynotes.R
import com.saurabh.mynotes.databinding.NoteLayoutBinding

class NoteAdapter(private val onFavoriteClick: (Note) -> Unit): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val itemBinding: NoteLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

        private val differCallback = object : DiffUtil.ItemCallback<Note>(){
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.noteDesc == newItem.noteDesc &&
                        oldItem.noteTitle == newItem.noteTitle
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
        val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder,position: Int) {
        val currentNote = differ.currentList[position]

        holder.itemBinding.noteTitle.text = currentNote.noteTitle
        holder.itemBinding.noteDesc.text = currentNote.noteDesc
        holder.itemBinding.addFavBtn.setImageResource(if (currentNote.isFavorite) R.drawable.favorite_icon else R.drawable.not_fav_icon)

        // Handle favorite button click
        holder.itemBinding.addFavBtn.setOnClickListener {
            currentNote.isFavorite = !currentNote.isFavorite // Toggle the favorite status
            holder.itemBinding.addFavBtn.setImageResource(
                if (currentNote.isFavorite) R.drawable.favorite_icon else R.drawable.not_fav_icon
            )
            onFavoriteClick(currentNote) // Notify the fragment about the status change
            // Submit the updated list to the adapter after toggling
            val updatedList = differ.currentList.toMutableList()
            differ.submitList(updatedList)
        }

        holder.itemView.setOnClickListener{
            val direction = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(currentNote)
            it.findNavController().navigate(direction)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}