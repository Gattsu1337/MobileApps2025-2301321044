package com.example.mangaverse.logic

import com.example.mangaverse.data.Manga
import com.example.mangaverse.data.MangaDao
import kotlinx.coroutines.flow.Flow

class MangaRepository(private val mangaDao: MangaDao) {
    val allManga: Flow<List<Manga>> = mangaDao.getAllManga()

    fun getManga(id: Int): Flow<Manga?> {
        return mangaDao.getMangaById(id)
    }

    suspend fun insert(manga: Manga) {
        mangaDao.insertManga(manga)
    }

    suspend fun update(manga: Manga) {
        mangaDao.updateManga(manga)
    }

    suspend fun delete(manga: Manga) {
        mangaDao.deleteManga(manga)
    }
}