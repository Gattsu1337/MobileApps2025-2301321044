package com.example.mangaverse.logic

import com.example.mangaverse.MainDispatcherRule
import com.example.mangaverse.data.Manga
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MangaViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<MangaRepository>(relaxed = true)

    private lateinit var viewModel: MangaViewModel

    @Before
    fun setup(){
        every { repository.allManga } returns flowOf(emptyList())

        viewModel = MangaViewModel(repository)
    }

    @Test
    fun `uiState initially loads empty list from repository`() = runTest{
        val state = viewModel.uiState.value
        assertEquals(emptyList<Manga>(), state.mangaList)
    }

    @Test
    fun `addManga calls repository insert`() = runTest {
        // Given
        val newManga = Manga(id = 1, title = "Naruto", author = "Kishimoto", currentChapter = 1, totalChapters = 700, status = "Reading", rating = 5f)

        // When
        viewModel.addManga(newManga)

        // Then
        // Verify that the repository's insert function was called exactly once with our manga
        coVerify(exactly = 1) { repository.insert(newManga) }
    }

    @Test
    fun `deleteManga calls repository delete`() = runTest {
        // Given
        val mangaToDelete = Manga(id = 2, title = "Bleach", author = "Kubo", currentChapter = 10, totalChapters = null, status = "Dropped", rating = 2f)

        // When
        viewModel.deleteManga(mangaToDelete)

        // Then
        coVerify(exactly = 1) { repository.delete(mangaToDelete) }
    }

}