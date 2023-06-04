package com.akaiz.noteapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akaiz.noteapp.ui.theme.BabyBlue
import com.akaiz.noteapp.ui.theme.LightGreen
import com.akaiz.noteapp.ui.theme.RedOrange
import com.akaiz.noteapp.ui.theme.RedPink
import com.akaiz.noteapp.ui.theme.Violet

@Entity
data class Note(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String) : Exception(message)