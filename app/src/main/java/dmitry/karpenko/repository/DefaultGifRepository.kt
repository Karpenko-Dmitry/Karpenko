package dmitry.karpenko.repository

import androidx.lifecycle.LiveData
import dmitry.karpenko.data.database.database.GifDatabase
import dmitry.karpenko.data.database.entity.Category
import dmitry.karpenko.data.database.entity.CurrentUserGifPosition
import dmitry.karpenko.data.database.entity.Gif
import dmitry.karpenko.network.DevelopersLiveServiceGenerator
import dmitry.karpenko.network.toGif
import dmitry.karpenko.network.toGifList
import java.io.IOException

class DefaultGifRepository(private val database: GifDatabase) : GifRepository {


    /* Загрузка объекта сущности Gif из БД.
     Если в БД нет соответсвующей записи, данные загружаются с помощью API сервера
    */
    override suspend fun getGif(
        category: Category,
        pos: Long
    ) : Gif {
        var gif = getGifFromDb(pos,category)
        return gif ?: getNetworkGif(pos,category)
    }

    /* Загрузка последней позиции последовательности гиф-изображений
        для данной категории
    */
    override fun getCurrentPosition(
        category: Category
    ) : LiveData<Long> {
        return database
            .currentUserGifPositionDao
            .getLiveDataPositionByCategory(category.id)
    }

    /* Изменение последней позиции последовательности гиф-изображений
        для данной категории
    */
    override suspend fun setCurruntPosition(
        category: Category,
        pos : Long
    ) {
        database
            .currentUserGifPositionDao
            .update(CurrentUserGifPosition(category.id,pos))
    }

    //Загрузка Гиф-изображения с помощью API сервера
    private suspend fun getNetworkGif(pos: Long,category: Category) = when(category){
            Category.RANDOM -> getAndCacheNetworkRandomGif(pos)
            else -> getAndCacheNetworkCategoryGif(pos,category)
    }

    /*Загрузка Гиф-изображения с помощью API сервера для категории "Random"
        Если данные были получены, то данные кешируются в БД и возвращаются из функции.
        Если данные не были получены, то пробрасывается исключение
     */
    private suspend fun getAndCacheNetworkRandomGif(pos : Long) : Gif {
        val request = DevelopersLiveServiceGenerator
            .randomGifClient()
            .getRandomGif()
        if (request.failure || !request.isSuccessful) {
            throw IOException(request.exception)
        }
        val gif = request.body.toGif(pos,Category.RANDOM.value)
        database.gifDao.insert(gif)
        return gif
    }

    /*Загрузка Гиф-изображения с помощью API сервера для категорий "Hot", "Top", "Latest"
        Если данные были получены, то данные кешируются в БД и возвращаются из функции.
        Если данные не были получены, то пробрасывается исключение.
        Данные загружаются постранично
     */
    private suspend fun getAndCacheNetworkCategoryGif(pos: Long,category: Category) : Gif {
        val request = DevelopersLiveServiceGenerator
            .categoryGifClient(category)
            .getListGif(pos)
        if (request.failure || !request.isSuccessful) {
            throw IOException(request.exception)
        }
        val gifList = request.body.result.toGifList(pos,category.value)
        database.gifDao.insertAll(gifList)
        return gifList[0]
    }

    /*Поиск в БД записи Gif по позиции и категории.
        Если поиск не удачен, возвращает null
     */
    private suspend fun getGifFromDb(pos: Long,category: Category) : Gif? {
        return database.gifDao.findGif(pos,category.value)
    }
}