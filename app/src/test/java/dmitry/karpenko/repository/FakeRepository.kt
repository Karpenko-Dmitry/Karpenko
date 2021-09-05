package dmitry.karpenko.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dmitry.karpenko.data.database.entity.Category
import dmitry.karpenko.data.database.entity.Gif

class FakeRepository(val gifs :Array<Gif>, val errorPos : Long) : GifRepository {

    val curPosByCategory : HashMap<String,Long> = hashMapOf(
        Category.RANDOM.value to 0,
        Category.LATEST.value to 0,
        Category.TOP.value to 0,
        Category.HOT.value to 0
    )

    override suspend fun getGif(category: Category, pos: Long): Gif {
        if (pos == errorPos) {
            throw IllegalStateException()
        }
        return gifs[pos.toInt()]
    }

    override fun getCurrentPosition(category: Category): LiveData<Long> {
        return MutableLiveData(curPosByCategory[category.value])
    }

    override suspend fun setCurruntPosition(category: Category, pos: Long) {
        curPosByCategory[category.value] = pos
    }
}