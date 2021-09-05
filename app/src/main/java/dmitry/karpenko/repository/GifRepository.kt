package dmitry.karpenko.repository

import androidx.lifecycle.LiveData
import dmitry.karpenko.data.database.entity.Category
import dmitry.karpenko.data.database.entity.Gif

interface GifRepository {

    suspend fun getGif(category: Category, pos: Long) : Gif

    fun getCurrentPosition(category: Category) : LiveData<Long>

    suspend fun setCurruntPosition(category: Category, pos : Long)

    fun hasPrevious(pos: Long) = pos > 0
}