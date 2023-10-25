package org.sesac.management.view.artist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sesac.management.data.local.Artist
import org.sesac.management.data.local.ArtistType
import org.sesac.management.data.local.Event
import org.sesac.management.data.local.Rate
import org.sesac.management.repository.ArtistRepository
import org.sesac.management.util.common.ARTIST

class ArtistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ArtistRepository(application)

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

    fun insertRate(rate: Rate) = viewModelScope.launch {
        repository.insertRate(rate)
    }

    fun updateRate(rateId: Int, artistId: Int) = viewModelScope.launch {
        repository.updateRate(rateId, artistId)
    }

    fun getAllRate() = viewModelScope.launch {
        _rateList.value = repository.getAllRate()
    }

    fun getRate(rateId: Int) = viewModelScope.launch {
        repository.getRate(rateId)
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
            Log.d(ARTIST, "getArtistById: ${getArtistDetail.value}")
        }
    }

    fun getEventFromArtistId(artistId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            getEventFromArtist = repository.getEventFromArtist(artistId)
            Log.d(ARTIST, "getArtistById: ${getArtistDetail.value}")
        }
    }

    fun deleteArtist(artist: Artist) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.deleteArtist(artist)
            Log.d(ARTIST, "getArtistById: ${getArtistDetail.value}")
        }
    }
}