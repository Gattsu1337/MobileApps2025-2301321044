package com.example.mangaverse.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mangaverse.data.Manga
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MangaVerseUiState(
    val mangaList: List<Manga> = emptyList()
)

class MangaViewModel(private val repository: MangaRepository) : ViewModel() {
    val uiState: StateFlow<MangaVerseUiState> = repository.allManga
        .map { MangaVerseUiState(mangaList = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MangaVerseUiState()
        )

    fun getMangaItem(id: Int) = repository.getManga((id))

    fun addManga(manga: Manga) = viewModelScope.launch{
        repository.insert(manga)
    }

    fun updateManga(manga: Manga) = viewModelScope.launch{
        repository.update(manga)
    }

    fun deleteManga(manga: Manga) = viewModelScope.launch{
        repository.delete(manga)
    }
}

class MangaViewModelFactory(private val repository: MangaRepository) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaViewModel::class.java)){
            return MangaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}