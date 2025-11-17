package com.example.mangaverse.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mangaverse.data.Manga
import com.example.mangaverse.logic.MangaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaListScreen (
    viewModel: MangaViewModel,
    onMangaClick: (Int) -> Unit,
    onAddMangaClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("MangaVerse Library") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMangaClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add New Manga")
            }
        }
    ) { paddingValues ->
        if (uiState.mangaList.isEmpty()){
            EmptyState(modifier = Modifier.padding(paddingValues))
        } else {
            MangaList(
                mangaList = uiState.mangaList,
                onMangaClick = onMangaClick,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your Library is Empty.",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MangaList(
    mangaList: List<Manga>,
    onMangaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(mangaList, key = { it.id }) { manga ->
            MangaListItem(manga = manga, onClick = { onMangaClick(manga.id) })
            HorizontalDivider()
        }
    }
}

@Composable
fun MangaListItem(manga: Manga, onClick: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)){
            Text(
                text = manga.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
            )
            Text(
                text = "Author: ${manga.author}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Ch. ${manga.currentChapter}/${manga.totalChapters ?: "?"}",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = manga.status,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}