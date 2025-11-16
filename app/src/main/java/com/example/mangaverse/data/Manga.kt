package com.example.mangaverse.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga_library")

data class Manga(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val currentChapter: Int,
    val totalChapters: Int?,
    val status: String,
    val rating: Float?
)