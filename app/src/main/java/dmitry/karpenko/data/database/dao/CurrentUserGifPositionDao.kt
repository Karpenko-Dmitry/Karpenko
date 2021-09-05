package dmitry.karpenko.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dmitry.karpenko.data.database.entity.CurrentUserGifPosition

@Dao
interface CurrentUserGifPositionDao {

    @Query("SELECT curPosition FROM CurrentUserGifPosition WHERE id = :id")
    fun getLiveDataPositionByCategory(id : Int) : LiveData<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert( userPosition : CurrentUserGifPosition)

    @Update
    suspend fun update( userPosition : CurrentUserGifPosition)
}