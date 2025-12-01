package com.example.mangaverse.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mangaverse.data.Manga
import com.example.mangaverse.logic.MangaViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlin.math.exp

val StatusOptions = listOf(
    "Reading", "Completed", "On Hold", "Dropped", "Want to Read"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(
    mangaId: Int, // 0 for Add, > 0 for Edit
    viewModel: MangaViewModel,
    onNavigateBack: () -> Unit
) {
    val isEditing = mangaId > 0
    val context = LocalContext.current


    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var currentChapter by remember { mutableStateOf("") }
    var totalChapters by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(StatusOptions.first()) }
    var rating by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var mangaToDelete: Manga? by remember { mutableStateOf(null) }

    val isTotalChaptersInvalid by remember(currentChapter, totalChapters){
        derivedStateOf{
            val current = currentChapter.toIntOrNull()
            val total = totalChapters.toIntOrNull()
            current != null && total != null && current > total
        }
    }

    LaunchedEffect(mangaId) {
        if (isEditing) {
            viewModel.getMangaItem(mangaId)
                .filterNotNull()
                .first()
                .let { manga ->
                    title = manga.title
                    author = manga.author
                    currentChapter = manga.currentChapter.toString()
                    totalChapters = manga.totalChapters?.toString() ?: ""
                    status = manga.status
                    rating = manga.rating?.toString() ?: ""
                }
        }
    }

    fun shareManga(){
        val shareText = """
            I'm currently reading '$title' on MangaVerse!
            Current Status: $status
            Progress: Chapter $currentChapter${if (totalChapters.isNotBlank()) "/$totalChapters" else ""}
            My Rating: ${if (rating.isNotBlank()) "$rating/5.0" else "Not rated yet"}
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share Manga")
        context.startActivity(shareIntent)
    }

    fun onSave() {
        if (title.isBlank() || isTotalChaptersInvalid) return

        fun String.toIntOrNullIfBlank() = this.toIntOrNull()

        val mangaToSave = Manga(
            id = if (isEditing) mangaId else 0,
            title = title.trim(),
            author = author.trim(),
            currentChapter = currentChapter.toIntOrNullIfBlank() ?: 0,
            totalChapters = totalChapters.toIntOrNullIfBlank(),
            status = status,
            rating = rating.toFloatOrNull(),
        )

        if (isEditing) {
            viewModel.updateManga(mangaToSave)
        } else {
            viewModel.addManga(mangaToSave)
        }
        onNavigateBack()
    }

    fun onDelete(manga: Manga) {
        viewModel.deleteManga(manga)
        onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Manga" else "Add New Manga") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = {
                            shareManga()
                        }){
                            Icon(Icons.Filled.Share, contentDescription = "Share Manga")
                        }

                        IconButton(onClick = {
                            mangaToDelete = Manga(
                                id = mangaId,
                                title = title,
                                author = author,
                                currentChapter = currentChapter.toIntOrNull() ?: 0,
                                totalChapters = totalChapters.toIntOrNull(),
                                status = status,
                                rating = rating.toFloatOrNull()
                            )
                        }
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title (Required)") },
                modifier = Modifier.fillMaxWidth(),
                isError = title.isBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = currentChapter,
                    onValueChange = { currentChapter = it.filter { char -> char.isDigit() } },
                    label = { Text("Current Chapter") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = totalChapters,
                    onValueChange = { totalChapters = it.filter { char -> char.isDigit() } },
                    label = { Text("Total Chapter (Optional)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isTotalChaptersInvalid,
                    supportingText = {
                        if (isTotalChaptersInvalid){
                            Text("Current Chapter must be less than Total Chapter")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Reading Status") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    StatusOptions.forEach { selectOption ->
                        DropdownMenuItem(
                            text = { Text(selectOption) },
                            onClick = {
                                status = selectOption
                                expanded = false
                                if (selectOption == "Completed" && totalChapters.isNotBlank()){
                                    currentChapter = totalChapters
                                }
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = rating,
                onValueChange = {
                    rating = it.filter { char -> char.isDigit() || char == '.' }
                },
                label = { Text("Your Rating (0.0 - 5.0)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = ::onSave,
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(if (isEditing) "Update Manga" else "Add to Library")
            }


        }
    }

    mangaToDelete?.let { manga ->
        AlertDialog(
            onDismissRequest = { mangaToDelete = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(manga)
                        mangaToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mangaToDelete = null }
                ) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm deletion") },
            text = {
                Text("Are you sure you want to remove '${manga.title}' from your library? This cannot be undone.")
            }
        )
    }
}