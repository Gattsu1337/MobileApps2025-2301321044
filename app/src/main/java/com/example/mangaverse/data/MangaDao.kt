package com.example.mangaverse.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertManga(manga: Manga)

    @Update
    suspend fun updateManga(manga: Manga)

    @Delete
    suspend fun deleteManga(manga: Manga)

    @Query("SELECT * FROM manga_library ORDER BY title ASC")
    fun getAllManga(): Flow<List<Manga>>

    @Query("SELECT * FROM manga_library WHERE id = :id")
    fun getMangaById(id: Int): Flow<Manga?>
}