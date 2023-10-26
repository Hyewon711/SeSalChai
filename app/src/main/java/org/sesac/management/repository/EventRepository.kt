package org.sesac.management.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.sesac.management.data.local.Event
import org.sesac.management.data.local.Manager
import org.sesac.management.data.local.dao.ArtistDAO
import org.sesac.management.data.local.dao.EventDAO
import org.sesac.management.data.local.dao.ManagerDAO
import org.sesac.management.util.common.ioScope

class EventRepository(
    private val eventDAO: EventDAO,
    private val manageDAO: ManagerDAO,
    private val artistDAO: ArtistDAO,
) {
    companion object {
        @Volatile
        private var instance: EventRepository? = null
        var getEventDetail = MutableLiveData<Event>()

        fun getInstance(eventDAO: EventDAO, manageDAO: ManagerDAO, artistDAO: ArtistDAO) =
            instance ?: synchronized(this) {
                instance ?: EventRepository(eventDAO, manageDAO, artistDAO).also { instance = it }
            }
    }

    // 행사 정보를 추가할 때 사용
    suspend fun insertEvent(event: Event) {
        eventDAO.insertEvent(event)
    }

    // 모든 행사 정보를 불러올 때 사용
    /**
     * Get event data
     * stateflow - flow
     * livedata - flow
     * livedata - livedata
     * 3가지 버전으로 구현
     * @author 진혁
     */
    // flow - flow
    fun getAllEvent() = eventDAO.getAllEvent()

    // livedata - flow
//    fun getSearchByEventID(eventId: Int) = eventDAO.getSearchByEventID(eventId) // id로 검색

    // livedata - livedata
    fun getSearchEvent(eventName: String): LiveData<List<Event>> =
        eventDAO.getSearchEvent(eventName) // 이름으로 검색

    /**
     * R : EventFragment에서 RecyclerView의 item을 클릭했을 때,
     * 클릭한 position의 eventId를 기준으로, EventDetailFragment에 일치하는 데이터를 불러오기 위한 메서드
     * eventId로 Event 테이블에 저장된 값을 가져온다.
     * @param eventId
     * @author 혜원
     */
    suspend fun getSearchByEventID(eventId: Int): Event {
        getEventDetail = asyncGetSearchByEventId(eventId)
        return getEventDetail.value!!
    }

    private suspend fun asyncGetSearchByEventId(eventId: Int): MutableLiveData<Event> {
        val returnedId = ioScope.async(Dispatchers.IO) {
            return@async eventDAO.getSearchByEventID(eventId)
        }.await()
        return CoroutineScope(Dispatchers.Main).async {
            getEventDetail.value = returnedId
            getEventDetail
        }.await()
    }

    // 가장 최근에 만들어진 데이터를 가져옵니다.
    suspend fun getMostRecentEvent() = eventDAO.getMostRecentEvent()

    // 행사 정보를 갱신할 때 사용
    suspend fun updateEvent(event: Event) {
        eventDAO.updateEvent(event)
    }

    // 행사 id로 삭제할 때 사용
    suspend fun deleteEvent(eventId: Int) {
        eventDAO.deleteEvent(eventId)
    }


    // 매니저에 하나로 mapping된 행사 정보와 아티스트 정보를 저장할 때 사용
    fun insertManager(manager: Manager) {
        manageDAO.insertManager(manager)
    }

    // 행사 정보 등록시 아티스트 목록을 보여주기 위한 메서드
    suspend fun getAllArtist() = artistDAO.getAllArtist()

//
//    /**
//     * R : EventFragment 상단의 Search layout을 통해 결과값을 가져오는 메서드
//     * eventName으로 Event 테이블에 저장된 값을 가져온다.
//     * @param eventId
//     * @author 혜원
//     */
//    suspend fun getSearchEvent(eventName: String): Event {
//        return asyncGetSearchEvent(eventName)
//    }
//
//    private suspend fun asyncGetSearchEvent(eventName: String): Event {
//        return coroutineIOScope.async(Dispatchers.IO) {
//            return@async eventDAO.getSearchEvent(eventName)
//        }.await()
//    }
//
//    /**
//     * D : Event 테이블에서 EventId와 일치하는 데이터를 삭제하는 메서드
//     *
//     * @param eventId
//     */
//    suspend fun deleteEvent(eventId: Int) {
//        return asyncDeleteEvent(eventId)
//    }
//
//    private suspend fun asyncDeleteEvent(eventId: Int) {
//        return coroutineIOScope.async(Dispatchers.IO) {
//            return@async eventDAO.deleteEvent(eventId)
//        }.await()
//    }
//
//    /**
//     * D : Manager 테이블에서 EventId와 일치하는 데이터를 삭제하는 메서드
//     *
//     * @param eventId
//     */
//    suspend fun deleteEventFromManager(eventId: Int) {
//        return asyncDeleteEventFromManager(eventId)
//    }
//
//    private suspend fun asyncDeleteEventFromManager(eventId: Int) {
//        return coroutineIOScope.async(Dispatchers.IO) {
//            return@async eventDAO.deleteEventFromManager(eventId)
//        }.await()
//    }
//
//    /**
//     * D :
//     *
//     * @param eventId
//     */
//    suspend fun deleteEventWithManager(eventId: Int) {
//        return asyncDeleteEventWithManager(eventId)
//    }
//
//    private suspend fun asyncDeleteEventWithManager(eventId: Int) {
//        return coroutineIOScope.async(Dispatchers.IO) {
//            return@async eventDAO.deleteArtistWithEvent(eventId)
//        }.await()
//    }
}