package dmitry.karpenko.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dmitry.karpenko.data.database.entity.Gif

@Dao
interface GifDao {

    @Query("SELECT * FROM GIF WHERE id = :id and category = :category")
    suspend fun findGif(id : Long, category : String) : Gif?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert( gif : Gif)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll( gifList : List<Gif>)
}