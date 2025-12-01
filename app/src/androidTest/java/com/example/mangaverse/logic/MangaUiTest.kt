package com.example.mangaverse.logic

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.mangaverse.data.Manga
import com.example.mangaverse.ui.screens.MangaList
import com.example.mangaverse.ui.theme.MangaVerseTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MangaUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mangaList_displaysItems_correctly() {
        val sampleData = listOf(
            Manga(
                id = 1,
                title = "Naruto",
                author = "Masashi Kishimoto",
                currentChapter = 10,
                totalChapters = 700,
                status = "Reading",
                rating = 5f
            ),
            Manga(
                id = 2,
                title = "Attack on Titan",
                author = "Hajime Isayama",
                currentChapter = 139,
                totalChapters = 139,
                status = "Completed",
                rating = 5f
            )
        )

        composeTestRule.setContent {
            MangaVerseTheme {
                // We test the MangaList composable directly to avoid needing
                // to mock the entire ViewModel and Database for a UI test.
                MangaList(
                    mangaList = sampleData,
                    onMangaClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Naruto").assertIsDisplayed()
        composeTestRule.onNodeWithText("Author: Masashi Kishimoto").assertIsDisplayed()

        composeTestRule.onNodeWithText("Attack on Titan").assertIsDisplayed()
        composeTestRule.onNodeWithText("Completed").assertIsDisplayed()
    }

    @Test
    fun mangaList_click_triggersCallback() {
        var clickedMangaId = -1
        val sampleManga = Manga(
            id = 99,
            title = "Click Me Manga",
            author = "Test Author",
            currentChapter = 1,
            totalChapters = null,
            status = "Reading",
            rating = 0f
        )

        composeTestRule.setContent {
            MangaVerseTheme {
                MangaList(
                    mangaList = listOf(sampleManga),
                    onMangaClick = { id -> clickedMangaId = id }
                )
            }
        }

        composeTestRule.onNodeWithText("Click Me Manga").performClick()

        assertEquals(99, clickedMangaId)
    }
}