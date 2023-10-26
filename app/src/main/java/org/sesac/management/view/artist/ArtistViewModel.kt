package org.sesac.management.view.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sesac.management.data.local.Artist
import org.sesac.management.data.local.ArtistType
import org.sesac.management.data.local.Event
import org.sesac.management.data.local.Rate
import org.sesac.management.repository.ArtistRepository

class ArtistViewModel(private val repository : ArtistRepository) : ViewModel() {

    var getAllArtist = MutableLiveData<List<Artist>>()
    var getArtistDetail = MutableLiveData<Artist>()
    var getEventFromArtist = MutableLiveData<List<Event>>()
    var insertArtist = MutableLiveData<List<Long>>()
    private var _rateList = MutableLiveData<List<Rate>>()
    val rateList: LiveData<List<Rate>>
        get() = _rateList

    fun getAllArtist() {
        viewModelScope.launch {
            val artist = repository.getAllArtist()
            getAllArtist.value = artist
        }
    }

    suspend fun insertArtist(artist: Artist) {
        CoroutineScope(Dispatchers.Main).launch {
            insertArtist.value = repository.insertArtist(artist)
        }
    }

    fun insertRateWithArtist(rate: Rate, artistId: Int) = viewModelScope.launch {
        repository.insertRateWithArtist(rate, artistId)
    }


    fun getSearchResult(keyword: String) {
        CoroutineScope(Dispatchers.Main).launch {
            getAllArtist.value = repository.getArtistByName(keyword)
        }
    }

    fun getArtistByType(type: ArtistType) {
        CoroutineScope(Dispatchers.Main).launch {
            getAllArtist.value = repository.getArtistByType(type)
        }
    }

    fun getArtistById(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            getArtistDetail.value = repository.getArtistById(id)
        }
    }

    fun getEventFromArtistId(artistId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            getEventFromArtist = repository.getEventFromArtist(artistId)
        }
    }

    fun deleteArtist(artist: Artist) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.deleteArtist(artist)
        }
    }


    class ArtistViewModelFactory(private val artistRepository: ArtistRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
                ArtistViewModel(artistRepository) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}
