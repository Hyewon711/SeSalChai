package org.sesac.management.data.room

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ManagerDAO {
    /**
     * C: artistId를 이용해 Manager table에 aritst,event 추가
     * @param Manager 객체 (ex Manager(eventId, artistId)
     * @return event
     */
    @Insert
    fun insertManager(vararg manager: Manager): List<Long>

}