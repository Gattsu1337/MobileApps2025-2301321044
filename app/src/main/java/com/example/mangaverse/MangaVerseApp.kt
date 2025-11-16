package com.example.mangaverse

import android.app.Application
import com.example.mangaverse.data.MangaDatabase

class MangaVerseApp: Application() {
    val database: MangaDatabase by lazy { MangaDatabase.getDatabase(this) }
}